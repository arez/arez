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
  }

  @Override
  protected void onBecomeStale()
  {
    schedule();
  }

  final boolean isScheduled()
  {
    return _scheduled;
  }

  final void schedule()
  {
    if ( !_scheduled )
    {
      _scheduled = true;
      getContext().scheduleReaction( this );
    }
  }
}
