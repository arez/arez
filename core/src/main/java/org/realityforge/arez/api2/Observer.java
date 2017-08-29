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
   * The stalest state of the associated observables that are also derivations.
   */
  @Nonnull
  private ObserverState _state = ObserverState.NOT_TRACKING;
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
   * Set the state of the observer.
   *
   * <ul>
   * <li>If the state changes from UP_TO_DATE to STALE or POSSIBLY_STALE then call the onBecomeStale hook method.</li>
   * <li>If the state changes to NOT_TRACKING then call the onBecomeUnobserved hook method.</li>
   * <li>If the state changes from NOT_TRACKING then call the onBecomeObserved hook method.</li>
   * </ul>
   *
   * @param state the state of the observer.
   */
  public final void setState( @Nonnull final ObserverState state )
  {
    if ( !state.equals( _state ) )
    {
      final ObserverState originalState = _state;
      _state = state;
      if ( ObserverState.UP_TO_DATE == originalState &&
           ( ObserverState.STALE == state || ObserverState.POSSIBLY_STALE == state ) )
      {
        onBecomeStale();
      }
      else if ( ObserverState.NOT_TRACKING == _state )
      {
        onBecomeUnobserved();
      }
      else if ( ObserverState.NOT_TRACKING == originalState )
      {
        onBecomeObserved();
      }
    }
  }

  protected void onBecomeObserved()
  {
  }

  protected void onBecomeUnobserved()
  {
  }

  protected void onBecomeStale()
  {
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
    Guards.invariant( () -> getDependencies().size() == new HashSet<>( getDependencies() ).size(),
                      () -> String.format(
                        "%s: The set of dependencies in observer named '%s' is not unique. Current list: '%s'.",
                        context,
                        getName(),
                        getDependencies().stream().map( Node::getName ).collect( Collectors.toList() ).toString() ) );
  }

  /**
   * Ensure all dependencies contain this observer in the list of observers.
   * Should be optimized away if invariant checking is disabled.
   *
   * @param context some useful debugging context used in invariant checks.
   */
  final void invariantDependenciesBackLink( @Nonnull final String context )
  {
    if ( ArezConfig.checkInvariants() )
    {
      getDependencies().forEach( observable ->
                                   Guards.invariant( () -> observable.getObservers().contains( this ),
                                                     () -> String.format(
                                                       "%s: Observer named '%s' has dependency observer named '%s' which does not contain observer in list of observers.",
                                                       context,
                                                       getName(),
                                                       observable.getName() ) ) );
    }
  }
}
