package org.realityforge.arez.api2;

import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
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

  @Test
  public void invokeObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final ReactionScheduler scheduler = new ReactionScheduler( context );

    final String name = ValueUtil.randomString();
    final TransactionMode mode = TransactionMode.READ_ONLY;

    final AtomicInteger callCount = new AtomicInteger();
    final AtomicInteger errorCount = new AtomicInteger();

    context.addObserverErrorHandler( ( observer, error, throwable ) -> errorCount.incrementAndGet() );

    final Reaction reaction = observer -> {
      callCount.incrementAndGet();
      assertEquals( observer.getContext(), context );
      assertEquals( context.isTransactionActive(), true );
      assertEquals( context.getTransaction().getName(), name );
      assertEquals( context.getTransaction().getMode(), mode );
      assertEquals( observer.getName(), name );
    };

    final Observer observer = new Observer( context, name, mode, reaction );

    scheduler.invokeObserver( observer );

    assertEquals( callCount.get(), 1 );
    assertEquals( errorCount.get(), 0 );
  }


  @Test
  public void invokeObserver_reactionGeneratesError()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final ReactionScheduler scheduler = new ReactionScheduler( context );

    final String name = ValueUtil.randomString();
    final TransactionMode mode = TransactionMode.READ_ONLY;

    final AtomicInteger callCount = new AtomicInteger();
    final AtomicInteger errorCount = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    context.addObserverErrorHandler( ( observer, error, throwable ) -> {
      errorCount.incrementAndGet();
      assertEquals( error, ObserverError.REACTION_ERROR );
      assertEquals( throwable, exception );
    } );

    final Reaction reaction = observer -> {
      callCount.incrementAndGet();
      throw exception;
    };

    final Observer observer = new Observer( context, name, mode, reaction );

    scheduler.invokeObserver( observer );

    assertEquals( callCount.get(), 1 );
    assertEquals( errorCount.get(), 1 );
  }

  @Test
  public void onRunawayReactionsDetected()
    throws Exception
  {
    getConfigProvider().setPurgeReactionsWhenRunawayDetected( true );

    final ArezContext context = new ArezContext();
    final ReactionScheduler scheduler = new ReactionScheduler( context );

    final Observer observer = new Observer( context, ValueUtil.randomString() );
    scheduler.getPendingObservers().add( observer );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, scheduler::onRunawayReactionsDetected );

    assertEquals( exception.getMessage(),
                  "Runaway reaction(s) detected. Observers still running after " +
                  scheduler.getMaxReactionRounds() + " rounds. Current observers include: [" +
                  observer.getName() + "]" );

    // Ensure observers purged
    assertEquals( scheduler.getPendingObservers().size(), 0 );
  }

  @Test
  public void onRunawayReactionsDetected_noPurgeCOnfigured()
    throws Exception
  {
    getConfigProvider().setPurgeReactionsWhenRunawayDetected( false );

    final ArezContext context = new ArezContext();
    final ReactionScheduler scheduler = new ReactionScheduler( context );

    final Observer observer = new Observer( context, ValueUtil.randomString() );
    scheduler.getPendingObservers().add( observer );

    assertThrows( IllegalStateException.class, scheduler::onRunawayReactionsDetected );

    // Ensure observers not purged
    assertEquals( scheduler.getPendingObservers().size(), 1 );
  }

  @Test
  public void onRunawayReactionsDetected_invariantCheckingDisabled()
    throws Exception
  {
    getConfigProvider().setCheckInvariants( false );

    final ArezContext context = new ArezContext();
    final ReactionScheduler scheduler = new ReactionScheduler( context );

    final Observer observer = new Observer( context, ValueUtil.randomString() );
    scheduler.getPendingObservers().add( observer );

    scheduler.onRunawayReactionsDetected();
  }
}
