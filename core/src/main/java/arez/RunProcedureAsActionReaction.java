package arez;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

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
    final Procedure action;
    if ( Arez.shouldCheckInvariants() )
    {
      action = () -> {
        _action.call();
        final Transaction current = Transaction.current();

        final ArrayList<Observable<?>> observables = current.getObservables();
        invariant( () -> Objects.requireNonNull( current.getTracker() ).isDisposing() ||
                         ( null != observables && !observables.isEmpty() ),
                   () -> "Arez-0172: Autorun observer named '" + observer.getName() + "' completed " +
                         "reaction but is not observing any observables and thus will never be rescheduled. " +
                         "This may not be an autorun candidate." );
      };
    }
    else
    {
      action = _action;
    }
    observer.getContext().action( Arez.areNamesEnabled() ? observer.getName() : null,
                                  Arez.shouldEnforceTransactionType() ? observer.getMode() : null,
                                  action,
                                  true,
                                  observer );
  }
}
