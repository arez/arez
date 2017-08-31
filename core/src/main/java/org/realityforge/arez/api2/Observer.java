package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A node within Arez that is notified of changes in 0 or more Observables.
 */
public class Observer
  extends Node
{
  /**
   * Hook action called when the Observer changes from the INACTIVE state to any other state.
   */
  @Nullable
  private Action _onActivate;
  /**
   * Hook action called when the Observer changes to the INACTIVE state to any other state.
   */
  @Nullable
  private Action _onDeactivate;
  /**
   * Hook action called when the Observer changes from the UP_TO_DATE state to STALE or POSSIBLY_STALE.
   */
  @Nullable
  private Action _onStale;
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

  Observer( @Nonnull final ArezContext context, @Nullable final String name )
  {
    super( context, name );
  }

  /**
   * Return the state of the observer.
   *
   * @return the state of the observer.
   */
  @Nonnull
  public final ObserverState getState()
  {
    return _state;
  }

  /**
   * Return true if the observer is active.
   * Being "active" means that the state of the observer is not {@link ObserverState#INACTIVE}.
   *
   * <p>An inactive observer has no dependencies and depending on the type of observer may
   * have other consequences. (i.e. An inactive {@link Reaction} will never be scheduled.</p>
   *
   * @return true if the Observer is active.
   */
  final boolean isActive()
  {
    return ObserverState.INACTIVE != getState();
  }

  /**
   * Return true if the observer is not active.
   * The inverse of {@link #isActive()}
   *
   * @return true if the Observer is inactive.
   */
  final boolean isInactive()
  {
    return !isActive();
  }

  /**
   * Set the state of the observer.
   * Call the hook actions for relevant state change.
   *
   * @param state the new state of the observer.
   */
  public final void setState( @Nonnull final ObserverState state )
  {
    invariantState();
    if ( !state.equals( _state ) )
    {
      final ObserverState originalState = _state;
      _state = state;
      if ( ObserverState.UP_TO_DATE == originalState &&
           ( ObserverState.STALE == state || ObserverState.POSSIBLY_STALE == state ) )
      {
        runHook( getOnStale(), ObserverError.ON_STALE_ERROR );
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
  private void runHook( @Nullable final Action hook, @Nonnull final ObserverError error )
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
  final void setOnActivate( @Nullable final Action onActivate )
  {
    _onActivate = onActivate;
  }

  /**
   * Return the onActivate hook.
   *
   * @return the onActivate hook.
   */
  @Nullable
  final Action getOnActivate()
  {
    return _onActivate;
  }

  /**
   * Set the onDeactivate hook.
   *
   * @param onDeactivate the hook.
   */
  final void setOnDeactivate( @Nullable final Action onDeactivate )
  {
    _onDeactivate = onDeactivate;
  }

  /**
   * Return the onDeactivate hook.
   *
   * @return the onDeactivate hook.
   */
  @Nullable
  final Action getOnDeactivate()
  {
    return _onDeactivate;
  }

  /**
   * Set the onStale hook.
   *
   * @param onStale the hook.
   */
  final void setOnStale( @Nullable final Action onStale )
  {
    _onStale = onStale;
  }

  /**
   * Return the onStale hook.
   *
   * @return the onStale hook.
   */
  @Nullable
  final Action getOnStale()
  {
    return _onStale;
  }

  /**
   * Remove all dependencies, removing this observer from all dependencies in the process.
   */
  final void clearDependencies()
  {
    getDependencies().forEach( dependency -> dependency.removeObserver( this ) );
    getDependencies().clear();
  }

  /**
   * Return the dependencies.
   *
   * @return the dependencies.
   */
  @Nonnull
  final ArrayList<Observable> getDependencies()
  {
    return _dependencies;
  }

  /**
   * Replace the current set of dependencies with supplied dependencies.
   * This should be the only mechanism via which the dependencies are updated.
   *
   * @param dependencies the new set of dependencies.
   */
  final void replaceDependencies( @Nonnull final ArrayList<Observable> dependencies )
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
  final void invariantDependenciesUnique( @Nonnull final String context )
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
  final void invariantDependenciesBackLink( @Nonnull final String context )
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
    }
  }

  /**
   * Ensure that state field and other fields of the Observer are consistent.
   */
  final void invariantState()
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
  }
}
