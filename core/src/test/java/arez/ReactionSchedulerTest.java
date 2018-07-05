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
    assertEquals( scheduler.getPendingDisposes().size(), 0 );

    assertEquals( scheduler.isReactionsRunning(), false );
    assertEquals( scheduler.areDisposesRunning(), false );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getMaxReactionRounds(), ReactionScheduler.DEFAULT_MAX_REACTION_ROUNDS );
  }

  @Test
  public void construction_Zones_Disabled()
    throws Exception
  {
    ArezTestUtil.disableZones();
    ArezTestUtil.resetState();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> new ReactionScheduler( Arez.context() ) );

    assertEquals( exception.getMessage(),
                  "Arez-0164: ReactionScheduler passed a context but Arez.areZonesEnabled() is false" );
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

    assertEquals( exception.getMessage(), "Arez-0098: Attempting to set maxReactionRounds to negative value -1." );
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
                  "Arez-0101: Runaway reaction(s) detected. Observers still running after " +
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
  public void scheduleDispose()
    throws Exception
  {
    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    final Observer observer = newReadOnlyObserver();

    assertEquals( scheduler.getPendingObservers().size(), 0 );

    scheduler.scheduleDispose( observer );

    assertEquals( scheduler.getPendingDisposes().size(), 1 );
    assertEquals( scheduler.getPendingDisposes().contains( observer ), true );
  }

  @Test
  public void scheduleDispose_alreadyScheduled()
    throws Exception
  {
    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    final Observer observer = newReadOnlyObserver();

    assertEquals( scheduler.getPendingObservers().size(), 0 );

    scheduler.scheduleDispose( observer );

    scheduler.getPendingDisposes().add( observer );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> scheduler.scheduleDispose( observer ) );

    assertEquals( exception.getMessage(),
                  "Arez-0165: Attempting to schedule disposable '" + observer.getName() +
                  "' when disposable is already pending." );
  }

  @Test
  public void runDispose()
    throws Exception
  {
    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    final Disposable disposable = newObservable();

    scheduler.scheduleDispose( disposable );

    assertEquals( disposable.isDisposed(), false );

    assertTrue( scheduler.runDispose() );

    assertEquals( disposable.isDisposed(), true );

    assertFalse( scheduler.runDispose() );
  }

  @Test
  public void runDisposeInsideTransaction()
    throws Exception
  {
    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    scheduler.scheduleDispose( newObservable() );

    setCurrentTransaction( newReadWriteObserver() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, scheduler::runDispose );

    assertEquals( exception.getMessage(),
                  "Arez-0156: Invoked runDispose when transaction named '" +
                  Arez.context().getTransaction().getName() + "' is active." );
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
  public void scheduleReaction_highPriorityObserver()
    throws Exception
  {
    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    final Observer observer1 = newReadOnlyObserver();
    final Observer observer2 = new Observer( Arez.context(),
                                             null,
                                             ValueUtil.randomString(),
                                             null,
                                             TransactionMode.READ_ONLY,
                                             new TestReaction(),
                                             Priority.HIGH,
                                             false );
    final Observer observer3 = newReadOnlyObserver();
    final Observer observer4 = new Observer( Arez.context(),
                                             null,
                                             ValueUtil.randomString(),
                                             null,
                                             TransactionMode.READ_ONLY,
                                             new TestReaction(),
                                             Priority.HIGH,
                                             false );

    assertEquals( scheduler.getPendingObservers().size(), 0 );

    scheduler.scheduleReaction( observer1 );
    scheduler.scheduleReaction( observer2 );
    scheduler.scheduleReaction( observer3 );
    scheduler.scheduleReaction( observer4 );

    assertEquals( scheduler.getPendingObservers( Priority.HIGH ).size(), 2 );
    assertEquals( scheduler.getPendingObservers( Priority.NORMAL ).size(), 2 );
    assertEquals( scheduler.getPendingObservers( Priority.LOW ).size(), 0 );
    assertEquals( scheduler.getPendingObservers( Priority.LOWEST ).size(), 0 );

    assertEquals( scheduler.getPendingObservers( Priority.HIGH ).get( 0 ), observer2 );
    assertEquals( scheduler.getPendingObservers( Priority.HIGH ).get( 1 ), observer4 );
    assertEquals( scheduler.getPendingObservers( Priority.NORMAL ).get( 0 ), observer1 );
    assertEquals( scheduler.getPendingObservers( Priority.NORMAL ).get( 1 ), observer3 );
  }

  @Test
  public void scheduleReaction_multipleObserverPriorities()
    throws Exception
  {
    final ReactionScheduler scheduler = new ReactionScheduler( Arez.context() );

    final Observer observer1 = new Observer( Arez.context(),
                                             null,
                                             ValueUtil.randomString(),
                                             null,
                                             TransactionMode.READ_ONLY,
                                             new TestReaction(),
                                             Priority.LOWEST,
                                             false );
    final Observer observer2 = new Observer( Arez.context(),
                                             null,
                                             ValueUtil.randomString(),
                                             null,
                                             TransactionMode.READ_ONLY,
                                             new TestReaction(),
                                             Priority.HIGH,
                                             false );
    final Observer observer3 = new Observer( Arez.context(),
                                             null,
                                             ValueUtil.randomString(),
                                             null,
                                             TransactionMode.READ_ONLY,
                                             new TestReaction(),
                                             Priority.NORMAL,
                                             false );
    final Observer observer4 = new Observer( Arez.context(),
                                             null,
                                             ValueUtil.randomString(),
                                             null,
                                             TransactionMode.READ_ONLY,
                                             new TestReaction(),
                                             Priority.LOW,
                                             false );
    final Observer observer5 = new Observer( Arez.context(),
                                             null,
                                             ValueUtil.randomString(),
                                             null,
                                             TransactionMode.READ_ONLY,
                                             new TestReaction(),
                                             Priority.LOWEST,
                                             false );
    final Observer observer6 = new Observer( Arez.context(),
                                             null,
                                             ValueUtil.randomString(),
                                             null,
                                             TransactionMode.READ_ONLY,
                                             new TestReaction(),
                                             Priority.HIGH,
                                             false );
    final Observer observer7 = new Observer( Arez.context(),
                                             null,
                                             ValueUtil.randomString(),
                                             null,
                                             TransactionMode.READ_ONLY,
                                             new TestReaction(),
                                             Priority.NORMAL,
                                             false );
    final Observer observer8 = new Observer( Arez.context(),
                                             null,
                                             ValueUtil.randomString(),
                                             null,
                                             TransactionMode.READ_ONLY,
                                             new TestReaction(),
                                             Priority.LOW,
                                             false );

    assertEquals( scheduler.getPendingObservers().size(), 0 );

    scheduler.scheduleReaction( observer1 );
    scheduler.scheduleReaction( observer2 );
    scheduler.scheduleReaction( observer3 );
    scheduler.scheduleReaction( observer4 );
    scheduler.scheduleReaction( observer5 );
    scheduler.scheduleReaction( observer6 );
    scheduler.scheduleReaction( observer7 );
    scheduler.scheduleReaction( observer8 );

    assertEquals( scheduler.getPendingObservers( Priority.HIGH ).size(), 2 );
    assertEquals( scheduler.getPendingObservers( Priority.NORMAL ).size(), 2 );
    assertEquals( scheduler.getPendingObservers( Priority.LOW ).size(), 2 );
    assertEquals( scheduler.getPendingObservers( Priority.LOWEST ).size(), 2 );

    assertEquals( scheduler.getPendingObservers( Priority.HIGH ).get( 0 ), observer2 );
    assertEquals( scheduler.getPendingObservers( Priority.HIGH ).get( 1 ), observer6 );
    assertEquals( scheduler.getPendingObservers( Priority.NORMAL ).get( 0 ), observer3 );
    assertEquals( scheduler.getPendingObservers( Priority.NORMAL ).get( 1 ), observer7 );
    assertEquals( scheduler.getPendingObservers( Priority.LOW ).get( 0 ), observer4 );
    assertEquals( scheduler.getPendingObservers( Priority.LOW ).get( 1 ), observer8 );
    assertEquals( scheduler.getPendingObservers( Priority.LOWEST ).get( 0 ), observer1 );
    assertEquals( scheduler.getPendingObservers( Priority.LOWEST ).get( 1 ), observer5 );
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
                  "Arez-0099: Attempting to schedule observer named '" + observer.getName() +
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
                  "Arez-0100: Invoked runObserver when transaction named '" +
                  Arez.context().getTransaction().getName() + "' is active." );
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
        new Observer( Arez.context(),
                      null,
                      ValueUtil.randomString(),
                      null,
                      TransactionMode.READ_WRITE,
                      reactions[ i ],
                      Priority.NORMAL,
                      false );
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
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_WRITE,
                    reaction,
                    Priority.NORMAL,
                    false );
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
                  "Arez-0101: Runaway reaction(s) detected. Observers still running after 20 rounds. " +
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
      new Observer( Arez.context(),
                    null,
                    ValueUtil.randomString(),
                    null,
                    TransactionMode.READ_ONLY,
                    reaction,
                    Priority.NORMAL,
                    false );
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
  public void runPendingTasks()
    throws Exception
  {
    final ReactionScheduler scheduler = Arez.context().getScheduler();

    setupReadWriteTransaction();

    final Disposable[] disposables = new Disposable[ 3 ];
    for ( int i = 0; i < disposables.length; i++ )
    {
      disposables[ i ] = newObservable();
      scheduler.scheduleDispose( disposables[ i ] );
    }

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
        new Observer( Arez.context(),
                      null,
                      ValueUtil.randomString(),
                      null,
                      TransactionMode.READ_WRITE,
                      reactions[ i ],
                      Priority.NORMAL,
                      false );
      observables[ i ] = newObservable();

      observers[ i ].setState( ObserverState.UP_TO_DATE );
      observables[ i ].addObserver( observers[ i ] );
      observers[ i ].getDependencies().add( observables[ i ] );

      //observer has reaction so setStale should result in reschedule
      observers[ i ].setState( ObserverState.STALE );
    }

    Transaction.setTransaction( null );

    assertEquals( scheduler.getPendingObservers().size(), observers.length );
    assertEquals( scheduler.getPendingDisposes().size(), disposables.length );

    scheduler.runPendingTasks();

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.isReactionsRunning(), false );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertEquals( scheduler.getPendingDisposes().size(), 0 );

    for ( final Disposable disposable : disposables )
    {
      assertTrue( Disposable.isDisposed( disposable ) );
    }
  }

  @Test
  public void runPendingTasks_onlyReactions()
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
        new Observer( Arez.context(),
                      null,
                      ValueUtil.randomString(),
                      null,
                      TransactionMode.READ_WRITE,
                      reactions[ i ],
                      Priority.NORMAL,
                      false );
      observables[ i ] = newObservable();

      observers[ i ].setState( ObserverState.UP_TO_DATE );
      observables[ i ].addObserver( observers[ i ] );
      observers[ i ].getDependencies().add( observables[ i ] );

      //observer has reaction so setStale should result in reschedule
      observers[ i ].setState( ObserverState.STALE );
    }

    Transaction.setTransaction( null );

    assertEquals( scheduler.getPendingObservers().size(), observers.length );

    scheduler.runPendingTasks();

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.isReactionsRunning(), false );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
  }

  @Test
  public void runPendingTasks_onlyDisposables()
    throws Exception
  {
    final ReactionScheduler scheduler = Arez.context().getScheduler();

    final Disposable[] disposables = new Disposable[ 3 ];
    for ( int i = 0; i < disposables.length; i++ )
    {
      disposables[ i ] = newObservable();
      scheduler.scheduleDispose( disposables[ i ] );
    }

    assertEquals( scheduler.getPendingDisposes().size(), disposables.length );

    scheduler.runPendingTasks();

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.isReactionsRunning(), false );
    assertEquals( scheduler.getPendingObservers().size(), 0 );

    for ( final Disposable disposable : disposables )
    {
      assertTrue( Disposable.isDisposed( disposable ) );
    }
  }

  @Test
  public void runPendingTasks_avoidRunningIfAlreadyRunning()
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

    assertEquals( scheduler.getPendingObservers().size(), 1 );
    scheduler.runPendingTasks();
    assertEquals( scheduler.getPendingObservers().size(), 1 );

    // Make t he scheduler think it is no longer running
    scheduler.setCurrentReactionRound( 0 );

    scheduler.runPendingTasks();
    assertEquals( scheduler.getPendingObservers().size(), 0 );
  }
}
