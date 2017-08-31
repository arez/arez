package org.realityforge.arez.api2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Reaction
  extends Observer
{
  /**
   * Flag indicating whether this reaction has been scheduled. i.e. has a pending reaction.
   */
  private boolean _scheduled;

  Reaction( @Nonnull final ArezContext context, @Nullable final String name )
  {
    super( context, name );
    setOnStale( this::schedule );
  }

  final boolean isScheduled()
  {
    return _scheduled;
  }

  final void schedule()
  {
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
}
