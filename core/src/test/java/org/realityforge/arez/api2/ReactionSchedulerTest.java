package org.realityforge.arez.api2;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ReactionSchedulerTest
  extends AbstractArezTest
{
  @Test
  public void construction()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final ReactionScheduler scheduler = new ReactionScheduler( context );

    assertEquals( scheduler.getContext(), context );

    assertEquals( scheduler.getPendingObservers().size(), 0 );

    assertEquals( scheduler.isRunningReactions(), false );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getMaxReactionRounds(), ReactionScheduler.DEFAULT_MAX_REACTION_ROUNDS );
  }

  @Test
  public void setMaxReactionRounds()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final ReactionScheduler scheduler = new ReactionScheduler( context );

    assertEquals( scheduler.getMaxReactionRounds(), ReactionScheduler.DEFAULT_MAX_REACTION_ROUNDS );

    scheduler.setMaxReactionRounds( 0 );
    assertEquals( scheduler.getMaxReactionRounds(), 0 );

    scheduler.setMaxReactionRounds( 1234 );
    assertEquals( scheduler.getMaxReactionRounds(), 1234 );
  }

  @Test
  public void setMaxReactionRounds_negativeValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final ReactionScheduler scheduler = new ReactionScheduler( context );

    assertEquals( scheduler.getMaxReactionRounds(), ReactionScheduler.DEFAULT_MAX_REACTION_ROUNDS );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> scheduler.setMaxReactionRounds( -1 ) );

    assertEquals( exception.getMessage(), "Attempting to set maxReactionRounds to negative value -1." );
  }
}
