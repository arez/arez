package arez;

import java.util.Objects;
import javax.annotation.Nonnull;

final class RunProcedureReaction
  implements Reaction
{
  private final Procedure _executable;

  RunProcedureReaction( @Nonnull final Procedure executable )
  {
    _executable = Objects.requireNonNull( executable );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void react( @Nonnull final Observer observer )
    throws Throwable
  {
    _executable.call();
  }
}
