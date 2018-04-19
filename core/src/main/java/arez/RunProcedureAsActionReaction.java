package arez;

import java.util.Objects;
import javax.annotation.Nonnull;

final class RunProcedureAsActionReaction
  implements Reaction
{
  private final Procedure _action;

  RunProcedureAsActionReaction( @Nonnull final Procedure action )
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
    observer.getContext().action( Arez.areNamesEnabled() ? observer.getName() : null,
                                  Arez.shouldEnforceTransactionType() ? observer.getMode() : null,
                                  _action,
                                  true,
                                  observer );
  }
}
