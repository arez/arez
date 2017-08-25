package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Observer
  extends Node
{
  /**
   * The stalest state of the associated observables that are also derivations.
   */
  @Nonnull
  private ObserverState _state = ObserverState.NOT_TRACKING;
  /**
   * The observables that this derivation is derived from.
   * This corresponds to the list of observables that were observed whilst
   * tracking the last derivation. This list should contain no duplicates.
   */
  @Nonnull
  private ArrayList<Observable> _dependencies = new ArrayList<>();

  Observer( @Nonnull final ArezContext context, @Nullable final String name )
  {
    super( context, name );
  }

  @Nonnull
  public final ObserverState getState()
  {
    return _state;
  }

  public final void setState( @Nonnull final ObserverState state )
  {
    final ObserverState originalState = _state;
    _state = state;
    if ( ObserverState.UP_TO_DATE == originalState &&
         ( ObserverState.STALE == state || ObserverState.POSSIBLY_STALE == state ) )
    {
      onBecomeStale();
    }
  }

  protected void onBecomeStale()
  {
  }

  @Nonnull
  final ArrayList<Observable> getDependencies()
  {
    return _dependencies;
  }

  /**
   * Replace the current set of dependencies with supplied dependencies.
   * This should be the only mechanism via which the dependencies are updated.
   */
  final void replaceDependencies( @Nonnull final ArrayList<Observable> dependencies )
  {
    invariantDependenciesUnique( "Pre replaceDependencies" );
    _dependencies = Objects.requireNonNull( dependencies );
    invariantDependenciesUnique( "Post replaceDependencies" );
    invariantDependenciesBackLink( "Post replaceDependencies" );
  }

  final void invariantDependenciesUnique( @Nonnull final String context )
  {
    Guards.invariant( () -> getDependencies().size() == new HashSet<>( getDependencies() ).size(),
                      () -> String.format(
                        "%s: The set of dependencies in observer named '%s' is not unique. Current list: '%s'.",
                        context,
                        getName(),
                        getDependencies().stream().map( Node::getName ).collect( Collectors.toList() ).toString() ) );
  }

  final void invariantDependenciesBackLink( @Nonnull final String context )
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
