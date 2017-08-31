package org.realityforge.arez.api2;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Reaction
  extends Observer
{
  /**
   * Flag indicating whether this reaction has been scheduled. i.e. has a pending reaction.
   */
  private boolean _scheduled;
  /**
   * The transaction mode in which the action executes.
   */
  @Nonnull
  private final TransactionMode _mode;

  Reaction( @Nonnull final ArezContext context, @Nullable final String name, @Nonnull final TransactionMode mode )
  {
    super( context, name );
    setOnStale( this::schedule );
    _mode = Objects.requireNonNull( mode );
  }

  /**
   * Return the transaction mode in which the action executes.
   *
   * @return the transaction mode in which the action executes.
   */
  @Nonnull
  final TransactionMode getMode()
  {
    return _mode;
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
