package arez;

import java.util.Objects;
import javax.annotation.Nonnull;

final class RunProcedureReaction
  implements Reaction
{
  private final Procedure _action;

  RunProcedureReaction( @Nonnull final Procedure action )
  {
    _action = Objects.requireNonNull( action );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void react( @Nonnull final Observer observer )
    throws Throwable
  {
    _action.call();
  }
}
