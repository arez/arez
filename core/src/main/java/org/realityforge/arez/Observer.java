package org.realityforge.arez;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A node within Arez that is notified of changes in 0 or more Observables.
 */
public final class Observer
  extends Node
{
  /**
   * The reference to the ComputedValue if this observer is a derivation.
   */
  @Nullable
  private final ComputedValue<?> _computedValue;
  /**
   * Hook action called when the Observer changes from the INACTIVE state to any other state.
   */
  @Nullable
  private Procedure _onActivate;
  /**
   * Hook action called when the Observer changes to the INACTIVE state to any other state.
   */
  @Nullable
  private Procedure _onDeactivate;
  /**
   * Hook action called when the Observer changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE.
   */
  @Nullable
  private Procedure _onStale;
  /**
   * The stalest state of the associated observables that are also derivations.
   */
  @Nonnull
  private ObserverState _state = ObserverState.INACTIVE;
  /**
   * The observables that this observer receives notifications from.
   * These are the dependencies within the dependency graph and will
   * typically correspond to the observables that were accessed in last
   * transaction that this observer was tracking.
   *
   * This list should contain no duplicates.
   */
  @Nonnull
  private ArrayList<Observable> _dependencies = new ArrayList<>();
  /**
   * Flag indicating whether this observer has been scheduled.
   * Should always be false unless _reaction is non-null.
   */
  private boolean _scheduled;
  /**
   * The transaction mode in which the observer executes.
   */
  @Nonnull
  private final TransactionMode _mode;
  /**
   * The code responsible for responding to changes if any.
   */
  @Nullable
  private final Reaction _reaction;
  /**
   * The memoized observable value created by observer if any.
   */
  @Nullable
  private final Observable _derivedValue;

  Observer( @Nonnull final ArezContext context, @Nullable final String name )
  {
    this( context, name, TransactionMode.READ_ONLY, null );
  }

  Observer( @Nonnull final ComputedValue<?> computedValue )
  {
    this( computedValue.getContext(),
          ArezConfig.enableNames() ? computedValue.getName() : null,
          computedValue,
          TransactionMode.READ_WRITE_OWNED,
          o -> computedValue.compute() );
    setOnStale( () -> getDerivedValue().reportPossiblyChanged() );
  }

  Observer( @Nonnull final ArezContext context,
            @Nullable final String name,
            @Nonnull final TransactionMode mode,
            @Nullable final Reaction reaction )
  {
    this( context, name, null, mode, reaction );
  }

  Observer( @Nonnull final ArezContext context,
            @Nullable final String name,
            @Nullable final ComputedValue<?> computedValue,
            @Nonnull final TransactionMode mode,
            @Nullable final Reaction reaction )
  {
    super( context, name );
    if ( TransactionMode.READ_WRITE_OWNED == mode )
    {
      Guards.invariant( () -> null != computedValue,
                        () -> String.format( "Attempted to construct an observer named '%s' with READ_WRITE_OWNED " +
                                             "transaction mode but no ComputedValue.", getName() ) );
    }
    else if ( null != computedValue )
    {
      Guards.fail( () -> String.format( "Attempted to construct an observer named '%s' with %s " +
                                        "transaction mode and a ComputedValue.", getName(), mode ) );
    }
    _computedValue = computedValue;
    _mode = Objects.requireNonNull( mode );
    _reaction = reaction;
    if ( TransactionMode.READ_WRITE_OWNED == mode )
    {
      _derivedValue = new Observable( context, name, this );
    }
    else
    {
      _derivedValue = null;
    }
  }

  @Nonnull
  Observable getDerivedValue()
  {
    Guards.invariant( this::isDerivation,
                      () -> String.format(
                        "Attempted to invoke getDerivedValue on observer named '%s' when is not a computed observer.",
                        getName() ) );
    assert null != _derivedValue;
    return _derivedValue;
  }

  boolean isDerivation()
  {
    /*
     * We do not use "null != _derivedValue" as it is called from constructor of observable
     * prior to assigning it to _derivedValue.
     */
    return TransactionMode.READ_WRITE_OWNED == getMode();
  }

  /**
   * Make the Observer INACTIVE and release any resources associated with observer.
   * The applications should NOT interact with the Observer after it has been disposed.
   */
  public void dispose()
  {
    getContext().safeProcedure( getName(), false, null, () -> setState( ObserverState.INACTIVE ) );
  }

  /**
   * Return the state of the observer.
   *
   * @return the state of the observer.
   */
  @Nonnull
  ObserverState getState()
  {
    return _state;
  }

  /**
   * Return the transaction mode in which the observer executes.
   *
   * @return the transaction mode in which the observer executes.
   */
  @Nonnull
  TransactionMode getMode()
  {
    return _mode;
  }

  /**
   * Return the reaction.
   *
   * @return the reaction.
   */
  @Nullable
  Reaction getReaction()
  {
    return _reaction;
  }

  /**
   * Return true if Observer has an associated reaction.
   *
   * @return true if Observer has an associated reaction.
   */
  boolean hasReaction()
  {
    return null != _reaction;
  }

  /**
   * Return true if the observer is active.
   * Being "active" means that the state of the observer is not {@link ObserverState#INACTIVE}.
   *
   * <p>An inactive observer has no dependencies and depending on the type of observer may
   * have other consequences. i.e. An inactive observer will never be scheduled even if it has a
   * reaction.</p>
   *
   * @return true if the Observer is active.
   */
  public boolean isActive()
  {
    return ObserverState.INACTIVE != getState();
  }

  /**
   * Return true if the observer is not active.
   * The inverse of {@link #isActive()}
   *
   * @return true if the Observer is inactive.
   */
  boolean isInactive()
  {
    return !isActive();
  }

  /**
   * Set the state of the observer.
   * Call the hook actions for relevant state change.
   *
   * @param state the new state of the observer.
   */
  void setState( @Nonnull final ObserverState state )
  {
    Guards.invariant( () -> getContext().isTransactionActive(),
                      () -> String.format(
                        "Attempt to invoke setState on observer named '%s' when there is no active transaction.",
                        getName() ) );
    invariantState();
    if ( !state.equals( _state ) )
    {
      final ObserverState originalState = _state;
      _state = state;
      if ( ObserverState.UP_TO_DATE == originalState &&
           ( ObserverState.STALE == state || ObserverState.POSSIBLY_STALE == state ) )
      {
        runHook( getOnStale(), ObserverError.ON_STALE_ERROR );
        if ( hasReaction() )
        {
          schedule();
        }
      }
      else if ( ObserverState.INACTIVE == _state )
      {
        runHook( getOnDeactivate(), ObserverError.ON_DEACTIVATE_ERROR );
        clearDependencies();
      }
      else if ( ObserverState.INACTIVE == originalState )
      {
        runHook( getOnActivate(), ObserverError.ON_ACTIVATE_ERROR );
      }
      invariantState();
    }
  }

  /**
   * Run the supplied hook if non null.
   *
   * @param hook the hook to run.
   */
  void runHook( @Nullable final Procedure hook, @Nonnull final ObserverError error )
  {
    if ( null != hook )
    {
      try
      {
        hook.call();
      }
      catch ( final Exception e )
      {
        getContext().getObserverErrorHandler().onObserverError( this, error, e );
      }
    }
  }

  /**
   * Set the onActivate hook.
   *
   * @param onActivate the hook.
   */
  void setOnActivate( @Nullable final Procedure onActivate )
  {
    _onActivate = onActivate;
  }

  /**
   * Return the onActivate hook.
   *
   * @return the onActivate hook.
   */
  @Nullable
  Procedure getOnActivate()
  {
    return _onActivate;
  }

  /**
   * Set the onDeactivate hook.
   *
   * @param onDeactivate the hook.
   */
  void setOnDeactivate( @Nullable final Procedure onDeactivate )
  {
    _onDeactivate = onDeactivate;
  }

  /**
   * Return the onDeactivate hook.
   *
   * @return the onDeactivate hook.
   */
  @Nullable
  Procedure getOnDeactivate()
  {
    return _onDeactivate;
  }

  /**
   * Set the onStale hook.
   *
   * @param onStale the hook.
   */
  void setOnStale( @Nullable final Procedure onStale )
  {
    _onStale = onStale;
  }

  /**
   * Return the onStale hook.
   *
   * @return the onStale hook.
   */
  @Nullable
  Procedure getOnStale()
  {
    return _onStale;
  }

  /**
   * Remove all dependencies, removing this observer from all dependencies in the process.
   */
  void clearDependencies()
  {
    getDependencies().forEach( dependency -> dependency.removeObserver( this ) );
    getDependencies().clear();
  }

  /**
   * Return true if this observer has a pending reaction.
   *
   * @return true if this observer has a pending reaction.
   */
  boolean isScheduled()
  {
    return _scheduled;
  }

  /**
   * Clear the scheduled flag. This is called when the observer's reaction is executed so it can be scheduled again.
   */
  void clearScheduledFlag()
  {
    _scheduled = false;
  }

  /**
   * Schedule this observer if it does not already have a reaction pending.
   *
   * This method should not be invoked unless {@link #hasReaction()} returns true.
   */
  void schedule()
  {
    Guards.invariant( this::hasReaction,
                      () -> String.format(
                        "Observer named '%s' has no reaction but an attempt has been made to schedule observer.",
                        getName() ) );
    Guards.invariant( this::isActive,
                      () -> String.format(
                        "Observer named '%s' is not active but an attempt has been made to schedule observer.",
                        getName() ) );
    if ( !_scheduled )
    {
      _scheduled = true;
      getContext().scheduleReaction( this );
    }
  }

  /**
   * Run the reaction in a transaction with the name and mode defined
   * by the observer. If the reaction throws an exception, the exception is reported
   * to the context global ObserverErrorHandlers
   */
  void invokeReaction()
  {
    clearScheduledFlag();
    final String name = ArezConfig.enableNames() ? getName() : null;
    final TransactionMode mode = getMode();
    final Reaction reaction = getReaction();
    Guards.invariant( () -> null != reaction,
                      () -> String.format(
                        "invokeReaction called on observer named '%s' but observer has no associated reaction.",
                        name ) );
    assert null != reaction;
    try
    {
      getContext().procedure( name, mode, this, () -> reaction.react( this ) );
    }
    catch ( final Throwable t )
    {
      getContext().getObserverErrorHandler().onObserverError( this, ObserverError.REACTION_ERROR, t );
    }
  }

  /**
   * Utility to mark all dependencies least stale observer as up to date.
   * Used when the Observer is determined to be up todate.
   */
  void markDependenciesLeastStaleObserverAsUpToDate()
  {
    for ( final Observable dependency : getDependencies() )
    {
      dependency.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
    }
  }

  /**
   * Return the dependencies.
   *
   * @return the dependencies.
   */
  @Nonnull
  ArrayList<Observable> getDependencies()
  {
    return _dependencies;
  }

  /**
   * Replace the current set of dependencies with supplied dependencies.
   * This should be the only mechanism via which the dependencies are updated.
   *
   * @param dependencies the new set of dependencies.
   */
  void replaceDependencies( @Nonnull final ArrayList<Observable> dependencies )
  {
    invariantDependenciesUnique( "Pre replaceDependencies" );
    _dependencies = Objects.requireNonNull( dependencies );
    invariantDependenciesUnique( "Post replaceDependencies" );
    invariantDependenciesBackLink( "Post replaceDependencies" );
  }

  /**
   * Ensure the dependencies list contain no duplicates.
   * Should be optimized away if invariant checking is disabled.
   *
   * @param context some useful debugging context used in invariant checks.
   */
  void invariantDependenciesUnique( @Nonnull final String context )
  {
    // This invariant check should not be needed but this guarantees the (GWT) optimizer removes this code
    if ( ArezConfig.checkInvariants() )
    {
      Guards.invariant( () -> getDependencies().size() == new HashSet<>( getDependencies() ).size(),
                        () -> String.format(
                          "%s: The set of dependencies in observer named '%s' is not unique. Current list: '%s'.",
                          context,
                          getName(),
                          getDependencies().stream().map( Node::getName ).collect( Collectors.toList() ).toString() ) );
    }
  }

  /**
   * Ensure all dependencies contain this observer in the list of observers.
   * Should be optimized away if invariant checking is disabled.
   *
   * @param context some useful debugging context used in invariant checks.
   */
  void invariantDependenciesBackLink( @Nonnull final String context )
  {
    // This invariant check should not be needed but this guarantees the (GWT) optimizer removes this code
    if ( ArezConfig.checkInvariants() )
    {
      getDependencies().forEach( observable ->
                                   Guards.invariant( () -> observable.getObservers().contains( this ),
                                                     () -> String.format(
                                                       "%s: Observer named '%s' has dependency observable named '%s' which does not contain the observer in the list of observers.",
                                                       context,
                                                       getName(),
                                                       observable.getName() ) ) );
      invariantDerivationState();
    }
  }

  /**
   * Ensure that state field and other fields of the Observer are consistent.
   */
  void invariantState()
  {
    if ( isInactive() )
    {
      Guards.invariant( () -> getDependencies().isEmpty(),
                        () -> String.format(
                          "Observer named '%s' is inactive but still has dependencies: %s.",
                          getName(),
                          getDependencies().stream().
                            map( Node::getName ).
                            collect( Collectors.toList() ) ) );
    }
    if ( isDerivation() )
    {
      Guards.invariant( () -> Objects.equals( getDerivedValue().getOwner(), this ),
                        () -> String.format(
                          "Observer named '%s' has a derived value that does not link back to observer.",
                          getName() ) );
    }
  }

  void invariantDerivationState()
  {
    if ( isDerivation() && isActive() )
    {
      Guards.invariant( () -> !getDerivedValue().getObservers().isEmpty() ||
                              Objects.equals( getContext().getTransaction().getTracker(), this ),
                        () -> String.format( "Observer named '%s' is a derivation and active but the derived " +
                                             "value has no observers.", getName() ) );
    }
  }

  /**
   * Return the ComputedValue for Observer.
   * This should not be called if observer is not a derivation and will generate an invariant failure
   * if invariants are enabled.
   *
   * @return the associated ComputedValue.
   */
  @Nonnull
  ComputedValue<?> getComputedValue()
  {
    Guards.invariant( this::isDerivation,
                      () -> String.format(
                        "Attempted to invoke getComputedValue on observer named '%s' when is not a computed observer.",
                        getName() ) );
    assert null != _computedValue;
    return _computedValue;
  }
}
