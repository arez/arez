package arez;

import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.BrainCheckTestUtil;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class ReactionSchedulerTest
  extends AbstractArezTest
{
  @Test
  public void construction()
    throws Exception
  {
    final ArezContext context = Arez.context();
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
    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

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
    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    assertEquals( scheduler.getMaxReactionRounds(), ReactionScheduler.DEFAULT_MAX_REACTION_ROUNDS );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> scheduler.setMaxReactionRounds( -1 ) );

    assertEquals( exception.getMessage(), "Attempting to set maxReactionRounds to negative value -1." );
  }

  @Test
  public void onRunawayReactionsDetected()
    throws Exception
  {
    ArezTestUtil.purgeReactionsWhenRunawayDetected();

    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    final Observer observer = newReadOnlyObserver();
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
    ArezTestUtil.noPurgeReactionsWhenRunawayDetected();

    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    final Observer observer = newReadOnlyObserver();
    scheduler.getPendingObservers().add( observer );

    assertThrows( IllegalStateException.class, scheduler::onRunawayReactionsDetected );

    // Ensure observers not purged
    assertEquals( scheduler.getPendingObservers().size(), 1 );
  }

  @Test
  public void onRunawayReactionsDetected_invariantCheckingDisabled()
    throws Exception
  {
    BrainCheckTestUtil.setCheckInvariants( false );

    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    final Observer observer = newReadOnlyObserver();
    scheduler.getPendingObservers().add( observer );

    scheduler.onRunawayReactionsDetected();
  }

  @Test
  public void scheduleReaction()
    throws Exception
  {
    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    final Observer observer = newReadOnlyObserver();

    assertEquals( scheduler.getPendingObservers().size(), 0 );

    scheduler.scheduleReaction( observer );

    assertEquals( scheduler.getPendingObservers().size(), 1 );
    assertEquals( scheduler.getPendingObservers().contains( observer ), true );
  }

  @Test
  public void scheduleReaction_observerAlreadyScheduled()
    throws Exception
  {
    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    final Observer observer = newReadOnlyObserver();

    scheduler.getPendingObservers().add( observer );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> scheduler.scheduleReaction( observer ) );

    assertEquals( exception.getMessage(),
                  "Attempting to schedule observer named '" + observer.getName() +
                  "' when observer is already pending." );
  }

  @Test
  public void runObserver_singlePendingObserver()
    throws Exception
  {
    final ReactionScheduler scheduler = Arez.context().getScheduler();

    final Observer observer = newReadWriteObserver();
    final TestReaction reaction = (TestReaction) observer.getReaction();
    assertNotNull( reaction );

    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );
    //observer has reaction so setStale should result in reschedule
    observer.setState( ObserverState.STALE );

    Transaction.setTransaction( null );

    assertEquals( observer.isScheduled(), true );
    assertEquals( scheduler.getPendingObservers().size(), 1 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );

    final boolean ran = scheduler.runObserver();

    assertEquals( ran, true );
    assertEquals( reaction.getCallCount(), 1 );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 1 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( observer.isScheduled(), false );

    reaction.assertObserver( 0, observer );

    final boolean ran2 = scheduler.runObserver();

    assertEquals( ran2, false );
    assertEquals( reaction.getCallCount(), 1 );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( observer.isScheduled(), false );
  }

  @Test
  public void runObserver_insideTransaction()
    throws Exception
  {
    final ReactionScheduler scheduler = Arez.context().getScheduler();

    final Observer observer = newReadWriteObserver();
    final TestReaction reaction = (TestReaction) observer.getReaction();
    assertNotNull( reaction );

    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );
    //observer has reaction so setStale should result in reschedule
    observer.setState( ObserverState.STALE );

    assertEquals( observer.isScheduled(), true );
    assertEquals( scheduler.getPendingObservers().size(), 1 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, scheduler::runObserver );

    assertEquals( exception.getMessage(),
                  "Invoked runObserver when transaction named '" +
                  Arez.context().getTransaction().getName() +
                  "' is active." );
  }

  @Test
  public void runObserver_multiplePendingObservers()
    throws Exception
  {
    final ReactionScheduler scheduler = Arez.context().getScheduler();

    setupReadWriteTransaction();

    final int round1Size = 10;
    final int round2Size = 4;
    final int round3Size = 1;
    final Observer[] observers = new Observer[ round1Size ];
    final Observable<?>[] observables = new Observable[ observers.length ];
    final TestReaction[] reactions = new TestReaction[ observers.length ];
    for ( int i = 0; i < observers.length; i++ )
    {
      final int currentIndex = i;
      if ( i != 0 && 0 == i % 2 )
      {
        reactions[ i ] = new TestReaction()
        {
          @Override
          protected void performReact( @Nonnull final Observer observer )
          {
            super.performReact( observer );
            if ( ( currentIndex == 8 && getCallCount() <= 2 ) || getCallCount() <= 1 )
            {
              observers[ currentIndex ].setState( ObserverState.STALE );
            }
          }
        };
      }
      else
      {
        reactions[ i ] = new TestReaction();
      }
      observers[ i ] =
        new Observer( Arez.context(), null, ValueUtil.randomString(), null, TransactionMode.READ_ONLY, reactions[ i ], false );
      observables[ i ] = newObservable();

      observers[ i ].setState( ObserverState.UP_TO_DATE );
      observables[ i ].addObserver( observers[ i ] );
      observers[ i ].getDependencies().add( observables[ i ] );

      //observer has reaction so setStale should result in reschedule
      observers[ i ].setState( ObserverState.STALE );
      assertEquals( observers[ i ].isScheduled(), true );
    }

    Transaction.setTransaction( null );

    assertEquals( scheduler.getPendingObservers().size(), observers.length );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );

    // Start from last observer and go down to first
    for ( int i = 0; i < round1Size; i++ )
    {
      assertEquals( scheduler.runObserver(), true );
      assertEquals( scheduler.getCurrentReactionRound(), 1 );
    }

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 1 );
    assertEquals( scheduler.getPendingObservers().size(), round2Size );
    assertEquals( scheduler.isReactionsRunning(), true );

    for ( int i = 0; i < round2Size; i++ )
    {
      assertEquals( scheduler.runObserver(), true );
      assertEquals( scheduler.getCurrentReactionRound(), 2 );
    }

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 2 );
    assertEquals( scheduler.getPendingObservers().size(), round3Size );
    assertEquals( scheduler.isReactionsRunning(), true );

    for ( int i = 0; i < round3Size; i++ )
    {
      assertEquals( scheduler.runObserver(), true );
      assertEquals( scheduler.getCurrentReactionRound(), 3 );
    }

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 3 );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertEquals( scheduler.isReactionsRunning(), true );

    assertEquals( scheduler.runObserver(), false );

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.isReactionsRunning(), false );
    assertEquals( scheduler.getPendingObservers().size(), 0 );

    assertEquals( reactions[ 0 ].getCallCount(), 1 );
    assertEquals( reactions[ 1 ].getCallCount(), 1 );
    assertEquals( reactions[ 2 ].getCallCount(), 2 );
    assertEquals( reactions[ 3 ].getCallCount(), 1 );
    assertEquals( reactions[ 4 ].getCallCount(), 2 );
    assertEquals( reactions[ 5 ].getCallCount(), 1 );
    assertEquals( reactions[ 6 ].getCallCount(), 2 );
    assertEquals( reactions[ 7 ].getCallCount(), 1 );
    assertEquals( reactions[ 8 ].getCallCount(), 3 );
    assertEquals( reactions[ 9 ].getCallCount(), 1 );
  }

  @Test( timeOut = 5000L )
  public void runObserver_RunawayReactionsDetected()
    throws Exception
  {
    ArezTestUtil.purgeReactionsWhenRunawayDetected();

    final ReactionScheduler scheduler = Arez.context().getScheduler();

    final Observer observer = newReadWriteObserver();

    setCurrentTransaction( observer );

    final TestReaction reaction = new TestReaction()
    {
      @Override
      protected void performReact( @Nonnull final Observer observer )
      {
        super.performReact( observer );
        observer.setState( ObserverState.STALE );
      }
    };
    final Observer toSchedule =
      new Observer( Arez.context(), null, ValueUtil.randomString(), null, TransactionMode.READ_ONLY, reaction, false );
    final Observable<?> observable = newObservable();

    toSchedule.setState( ObserverState.UP_TO_DATE );
    observable.addObserver( toSchedule );
    toSchedule.getDependencies().add( observable );

    //observer has reaction so setStale should result in reschedule
    toSchedule.setState( ObserverState.STALE );
    assertEquals( toSchedule.isScheduled(), true );

    Transaction.setTransaction( null );

    scheduler.setMaxReactionRounds( 20 );

    final AtomicInteger reactionCount = new AtomicInteger();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> {
        while ( scheduler.runObserver() )
        {
          reactionCount.incrementAndGet();
        }
      } );
    assertEquals( exception.getMessage(),
                  "Runaway reaction(s) detected. Observers still running after 20 rounds. " +
                  "Current observers include: [" + toSchedule.getName() + "]" );

    assertEquals( reactionCount.get(), 20 );

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.isReactionsRunning(), false );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
  }

  @Test( timeOut = 5000L )
  public void runObserver_RunawayReactionsDetected_invariantChecksDisabled()
    throws Exception
  {
    ArezTestUtil.purgeReactionsWhenRunawayDetected();
    BrainCheckTestUtil.setCheckInvariants( false );

    final ReactionScheduler scheduler = Arez.context().getScheduler();

    final Observer observer = newReadOnlyObserver();

    setCurrentTransaction( observer );

    final TestReaction reaction = new TestReaction()
    {
      @Override
      public void react( @Nonnull final Observer observer )
        throws Exception
      {
        super.react( observer );
        observer.setState( ObserverState.STALE );
      }
    };
    final Observer toSchedule =
      new Observer( Arez.context(), null, ValueUtil.randomString(), null, TransactionMode.READ_ONLY, reaction, false );
    final Observable<?> observable = newObservable();

    toSchedule.setState( ObserverState.UP_TO_DATE );
    observable.addObserver( toSchedule );
    toSchedule.getDependencies().add( observable );

    //observer has reaction so setStale should result in reschedule
    toSchedule.setState( ObserverState.STALE );
    assertEquals( toSchedule.isScheduled(), true );

    Transaction.setTransaction( null );

    scheduler.setMaxReactionRounds( 20 );

    final AtomicInteger reactionCount = new AtomicInteger();
    while ( scheduler.runObserver() )
    {
      reactionCount.incrementAndGet();
    }

    assertEquals( reactionCount.get(), 20 );

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.isReactionsRunning(), false );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
  }

  @Test
  public void runPendingObservers()
    throws Exception
  {
    final ReactionScheduler scheduler = Arez.context().getScheduler();

    setupReadWriteTransaction();

    final Observer[] observers = new Observer[ 10 ];
    final Observable[] observables = new Observable[ observers.length ];
    final TestReaction[] reactions = new TestReaction[ observers.length ];
    for ( int i = 0; i < observers.length; i++ )
    {
      final int currentIndex = i;
      if ( i != 0 && 0 == i % 2 )
      {
        reactions[ i ] = new TestReaction()
        {
          @Override
          protected void performReact( @Nonnull final Observer observer )
          {
            super.performReact( observer );
            if ( ( currentIndex == 8 && getCallCount() <= 2 ) || getCallCount() <= 1 )
            {
              observers[ currentIndex ].setState( ObserverState.STALE );
            }
          }
        };
      }
      else
      {
        reactions[ i ] = new TestReaction();
      }
      observers[ i ] =
        new Observer( Arez.context(), null, ValueUtil.randomString(), null, TransactionMode.READ_ONLY, reactions[ i ], false );
      observables[ i ] = newObservable();

      observers[ i ].setState( ObserverState.UP_TO_DATE );
      observables[ i ].addObserver( observers[ i ] );
      observers[ i ].getDependencies().add( observables[ i ] );

      //observer has reaction so setStale should result in reschedule
      observers[ i ].setState( ObserverState.STALE );
    }

    Transaction.setTransaction( null );

    assertEquals( scheduler.runPendingObservers(), 15 );

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.isReactionsRunning(), false );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
  }

  @Test
  public void runPendingObservers_avoidRunningIfAlreadyRunning()
    throws Exception
  {
    final ReactionScheduler scheduler = Arez.context().getScheduler();

    final Observer observer = newReadWriteObserver();

    setCurrentTransaction( observer );

    observer.setState( ObserverState.UP_TO_DATE );
    //observer has reaction so setStale should result in reschedule
    observer.setState( ObserverState.STALE );

    Transaction.setTransaction( null );

    assertEquals( observer.isScheduled(), true );
    assertEquals( scheduler.getPendingObservers().size(), 1 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );

    // Trick the schedule into thinking that it is currently running
    scheduler.setCurrentReactionRound( 1 );

    assertEquals( scheduler.runPendingObservers(), 0 );

    // Make t he scheduler think it is no longer running
    scheduler.setCurrentReactionRound( 0 );

    assertEquals( scheduler.runPendingObservers(), 1 );
  }
}
