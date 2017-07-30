package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public class Derivation
  extends Node
  implements Observer
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
  private final ArrayList<Observable> _dependencies = new ArrayList<>();

  Derivation( @Nonnull final ArezContext context, @Nonnull final String name )
  {
    super( context, name );
  }

  @Nonnull
  @Override
  public final ObserverState getState()
  {
    return _state;
  }

  @Override
  public final void setState( @Nonnull final ObserverState state )
  {
    _state = state;
    if ( state == ObserverState.STALE || state == ObserverState.POSSIBLY_STALE )
    {
      onBecomeStale();
    }
  }

  private void onBecomeStale()
  {
  }

  @Nonnull
  final ArrayList<Observable> getDependencies()
  {
    return _dependencies;
  }

  final void invariantDependenciesUnique()
  {

    Guards.invariant( () -> getDependencies().size() == new HashSet<>( getDependencies() ).size(),
                      () -> String.format(
                        "The set of dependencies in derivation named '%s' is not unique. Current list: '%s'.",
                        getName(),
                        getDependencies().stream().map( Node::getName ).collect( Collectors.toList() ).toString() ) );
  }
}
