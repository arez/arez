package org.realityforge.arez.api2;

import javax.annotation.Nonnull;

/**
 * A derivation that calculates a value and produces an observable value.
 *
 * The ComputedValue will remember the result of the computation for the duration
 * of a batch, or while being observed.
 *
 * During this time the ComputedValue will recompute only when one of its direct
 * dependencies has changed, but only when it is being accessed with `ComputedValue.get()`.
 *
 * Implementation description:
 * 1. First time it's being accessed it will compute and remember result
 *    give back remembered result until 2. happens
 * 2. First time any deep dependency change, propagate POSSIBLY_STALE to all observers, wait for 3.
 * 3. When it's being accessed, recompute if any shallow dependency changed.
 *    if result changed: propagate STALE to all observers, that were POSSIBLY_STALE from the last step.
 *    go to step 2. either way
 *
 * If at any point it's outside batch and it isn't observed: reset everything and go to 1.
 */
public final class ComputedValue
  extends Derivation
  implements Observer
{
  /**
   * The value that is computed from derivation.
   */
  @Nonnull
  private final Observable _observable;

  public ComputedValue( @Nonnull final ArezContext context, @Nonnull final String name )
  {
    super( context, name );
    _observable = new Observable( context, this );
  }

  @Nonnull
  public Observable getObservable()
  {
    return _observable;
  }
}
