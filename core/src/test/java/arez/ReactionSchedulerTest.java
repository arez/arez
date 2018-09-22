package arez;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
    final ReactionScheduler scheduler = context.getScheduler();

    assertEquals( scheduler.getContext(), context );

    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertEquals( scheduler.getPendingDisposes().size(), 0 );

    assertFalse( scheduler.isReactionsRunning() );
    assertFalse( scheduler.areDisposesRunning() );
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

    assertInvariantFailure( () -> new ReactionScheduler( Arez.context() ),
                            "Arez-0164: ReactionScheduler passed a context but Arez.areZonesEnabled() is false" );

  }

  @Test
  public void setMaxReactionRounds()
    throws Exception
  {
    final ReactionScheduler scheduler = Arez.context().getScheduler();

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
    final ReactionScheduler scheduler = Arez.context().getScheduler();

    assertEquals( scheduler.getMaxReactionRounds(), ReactionScheduler.DEFAULT_MAX_REACTION_ROUNDS );

    assertInvariantFailure( () -> scheduler.setMaxReactionRounds( -1 ),
                            "Arez-0098: Attempting to set maxReactionRounds to negative value -1." );
  }

  @Test
  public void onRunawayReactionsDetected()
    throws Exception
  {
    ArezTestUtil.purgeReactionsWhenRunawayDetected();

    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = context.observer( new CountAndObserveProcedure() );
    scheduler.getPendingObservers().add( observer );

    assertInvariantFailure( scheduler::onRunawayReactionsDetected,
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

    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = context.observer( new CountAndObserveProcedure() );
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

    final ReactionScheduler scheduler = Arez.context().getScheduler();

    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    scheduler.getPendingObservers().add( observer );

    scheduler.onRunawayReactionsDetected();
  }

  @Test
  public void scheduleDispose()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( scheduler.getPendingObservers().size(), 0 );

    scheduler.scheduleDispose( observer );

    assertEquals( scheduler.getPendingDisposes().size(), 1 );
    assertTrue( scheduler.getPendingDisposes().contains( observer ) );
  }

  @Test
  public void scheduleDispose_alreadyScheduled()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( scheduler.getPendingObservers().size(), 0 );

    scheduler.scheduleDispose( observer );

    scheduler.getPendingDisposes().add( observer );

    assertInvariantFailure( () -> scheduler.scheduleDispose( observer ),
                            "Arez-0165: Attempting to schedule disposable '" + observer.getName() +
                            "' when disposable is already pending." );
  }

  @Test
  public void runDispose()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Disposable disposable = context.observable();

    scheduler.scheduleDispose( disposable );

    assertFalse( disposable.isDisposed() );

    assertTrue( scheduler.runDispose() );

    assertTrue( disposable.isDisposed() );

    assertFalse( scheduler.runDispose() );
  }

  @Test
  public void runDisposeInsideTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    assertFalse( scheduler.hasTasksToSchedule() );

    scheduler.scheduleDispose( context.observable() );

    assertTrue( scheduler.hasTasksToSchedule() );

    setCurrentTransaction( newReadWriteObserver( context ) );

    assertInvariantFailure( scheduler::runDispose, "Arez-0156: Invoked runDispose when transaction named '" +
                                                   context.getTransaction().getName() + "' is active." );
  }

  @Test
  public void scheduleReaction()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertFalse( scheduler.hasTasksToSchedule() );

    scheduler.scheduleReaction( observer );

    assertEquals( scheduler.getPendingObservers().size(), 1 );
    assertTrue( scheduler.getPendingObservers().contains( observer ) );
    assertTrue( scheduler.hasTasksToSchedule() );
  }

  @Test
  public void scheduleReaction_highPriorityObserver()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer0 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_HIGHEST );
    final Observer observer1 = context.observer( new CountAndObserveProcedure() );
    final Observer observer2 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_HIGH );
    final Observer observer3 = context.observer( new CountAndObserveProcedure() );
    final Observer observer4 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_HIGH );

    context.triggerScheduler();

    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertFalse( scheduler.hasTasksToSchedule() );

    scheduler.scheduleReaction( observer0 );
    scheduler.scheduleReaction( observer1 );
    scheduler.scheduleReaction( observer2 );
    scheduler.scheduleReaction( observer3 );
    scheduler.scheduleReaction( observer4 );

    assertTrue( scheduler.hasTasksToSchedule() );

    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_HIGHEST ) ).size(), 1 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_HIGH ) ).size(), 2 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) ).size(), 2 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_LOW ) ).size(), 0 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_LOWEST ) ).size(), 0 );

    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_HIGHEST ) ).get( 0 ),
                  observer0 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_HIGH ) ).get( 0 ), observer2 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_HIGH ) ).get( 1 ), observer4 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) ).get( 0 ),
                  observer1 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) ).get( 1 ),
                  observer3 );
  }

  @Test
  public void scheduleReaction_multipleObserverPriorities()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer0 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_HIGHEST );
    final Observer observer1 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_LOWEST );
    final Observer observer2 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_HIGH );
    final Observer observer3 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_NORMAL );
    final Observer observer4 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_LOW );
    final Observer observer5 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_LOWEST );
    final Observer observer6 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_HIGH );
    final Observer observer7 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_NORMAL );
    final Observer observer8 = new Observer( context,
                                             null,
                                             ValueUtil.randomString(),
                                             new CountAndObserveProcedure(),
                                             new CountingProcedure(),
                                             Flags.PRIORITY_LOW );

    context.triggerScheduler();

    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertFalse( scheduler.hasTasksToSchedule() );

    scheduler.scheduleReaction( observer0 );
    scheduler.scheduleReaction( observer1 );
    scheduler.scheduleReaction( observer2 );
    scheduler.scheduleReaction( observer3 );
    scheduler.scheduleReaction( observer4 );
    scheduler.scheduleReaction( observer5 );
    scheduler.scheduleReaction( observer6 );
    scheduler.scheduleReaction( observer7 );
    scheduler.scheduleReaction( observer8 );

    assertTrue( scheduler.hasTasksToSchedule() );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_HIGHEST ) ).size(), 1 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_HIGH ) ).size(), 2 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) ).size(), 2 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_LOW ) ).size(), 2 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_LOWEST ) ).size(), 2 );

    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_HIGHEST ) ).get( 0 ),
                  observer0 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_HIGH ) ).get( 1 ), observer6 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_HIGH ) ).get( 1 ), observer6 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) ).get( 0 ),
                  observer3 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) ).get( 1 ),
                  observer7 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_LOW ) ).get( 0 ), observer4 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_LOW ) ).get( 1 ), observer8 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_LOWEST ) ).get( 0 ),
                  observer1 );
    assertEquals( scheduler.getPendingObservers( Flags.getPriorityIndex( Flags.PRIORITY_LOWEST ) ).get( 1 ),
                  observer5 );
  }

  @Test
  public void scheduleReaction_observerAlreadyScheduled()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    scheduler.getPendingObservers().add( observer );

    assertInvariantFailure( () -> scheduler.scheduleReaction( observer ),
                            "Arez-0099: Attempting to schedule observer named '" + observer.getName() +
                            "' when observer is already pending." );
  }

  @Test
  public void runObserver_singlePendingObserver()
    throws Exception
  {
    final ArezContext context = Arez.context();
    context.markSchedulerAsActive();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = newReadWriteObserver( context );
    final CountingProcedure observed = (CountingProcedure) observer.getObserved();
    assertNotNull( observed );

    final CountingProcedure onDepsChanged = (CountingProcedure) observer.getOnDepsChanged();
    assertNull( onDepsChanged );

    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );
    //observer has observed so setStale should result in reschedule
    observer.setState( Flags.STATE_STALE );

    Transaction.setTransaction( null );

    assertTrue( observer.isScheduled() );
    assertEquals( scheduler.getPendingObservers().size(), 1 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );

    final boolean ran = scheduler.runObserver();

    assertTrue( ran );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 1 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertFalse( observer.isScheduled() );

    final boolean ran2 = scheduler.runObserver();

    assertFalse( ran2 );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertFalse( observer.isScheduled() );
  }

  @Test
  public void runObserver_insideTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = newReadWriteObserver( context );
    final CountingProcedure observed = (CountingProcedure) observer.getObserved();
    assertNotNull( observed );

    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );
    //observer has observed so setStale should result in reschedule
    observer.setState( Flags.STATE_STALE );

    assertTrue( observer.isScheduled() );
    assertEquals( scheduler.getPendingObservers().size(), 1 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );

    assertInvariantFailure( scheduler::runObserver, "Arez-0100: Invoked runObserver when transaction named '" +
                                                    context.getTransaction().getName() + "' is active." );
  }

  @Test
  public void runObserver_multiplePendingObservers()
    throws Exception
  {
    final ArezContext context = Arez.context();
    context.markSchedulerAsActive();
    final ReactionScheduler scheduler = context.getScheduler();

    setupReadWriteTransaction();
    // Purge the observer that was scheduled
    scheduler.getPendingObservers().clear();

    final int round1Size = 10;
    final int round2Size = 4;
    final int round3Size = 1;
    final Observer[] observers = new Observer[ round1Size ];
    final ObservableValue<?>[] observableValues = new ObservableValue[ observers.length ];
    final CountingProcedure[] observeds = new CountingProcedure[ observers.length ];
    for ( int i = 0; i < observers.length; i++ )
    {
      final int currentIndex = i;
      if ( i != 0 && 0 == i % 2 )
      {
        observeds[ i ] = new CountingProcedure()
        {
          @Override
          public void call()
            throws Throwable
          {
            super.call();
            if ( ( currentIndex == 8 && getCallCount() <= 2 ) || getCallCount() <= 1 )
            {
              observers[ currentIndex ].reportStale();
            }
          }
        };
      }
      else
      {
        observeds[ i ] = new CountingProcedure();
      }
      observers[ i ] =
        new Observer( context,
                      null,
                      ValueUtil.randomString(),
                      observeds[ i ],
                      null,
                      Flags.READ_WRITE | Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );
      observableValues[ i ] = context.observable();

      observers[ i ].setState( Flags.STATE_UP_TO_DATE );
      observableValues[ i ].rawAddObserver( observers[ i ] );
      observers[ i ].getDependencies().add( observableValues[ i ] );

      //observer has observed so setStale should result in reschedule
      observers[ i ].setState( Flags.STATE_STALE );
      assertTrue( observers[ i ].isScheduled() );
    }

    Transaction.setTransaction( null );

    assertEquals( scheduler.getPendingObservers().size(), observers.length );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );

    // Start from last observer and go down to first
    for ( int i = 0; i < round1Size; i++ )
    {
      assertTrue( scheduler.runObserver() );
      assertEquals( scheduler.getCurrentReactionRound(), 1 );
    }

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 1 );
    assertEquals( scheduler.getPendingObservers().size(), round2Size );
    assertTrue( scheduler.isReactionsRunning() );

    for ( int i = 0; i < round2Size; i++ )
    {
      assertTrue( scheduler.runObserver() );
      assertEquals( scheduler.getCurrentReactionRound(), 2 );
    }

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 2 );
    assertEquals( scheduler.getPendingObservers().size(), round3Size );
    assertTrue( scheduler.isReactionsRunning() );

    for ( int i = 0; i < round3Size; i++ )
    {
      assertTrue( scheduler.runObserver() );
      assertEquals( scheduler.getCurrentReactionRound(), 3 );
    }

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 3 );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
    assertTrue( scheduler.isReactionsRunning() );

    assertFalse( scheduler.runObserver() );

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertFalse( scheduler.isReactionsRunning() );
    assertEquals( scheduler.getPendingObservers().size(), 0 );

    assertEquals( observeds[ 0 ].getCallCount(), 1 );
    assertEquals( observeds[ 1 ].getCallCount(), 1 );
    assertEquals( observeds[ 2 ].getCallCount(), 2 );
    assertEquals( observeds[ 3 ].getCallCount(), 1 );
    assertEquals( observeds[ 4 ].getCallCount(), 2 );
    assertEquals( observeds[ 5 ].getCallCount(), 1 );
    assertEquals( observeds[ 6 ].getCallCount(), 2 );
    assertEquals( observeds[ 7 ].getCallCount(), 1 );
    assertEquals( observeds[ 8 ].getCallCount(), 3 );
    assertEquals( observeds[ 9 ].getCallCount(), 1 );
  }

  @Test( timeOut = 5000L )
  public void runObserver_RunawayReactionsDetected()
    throws Exception
  {
    ArezTestUtil.purgeReactionsWhenRunawayDetected();
    setIgnoreObserverErrors( true );

    final AtomicReference<Observer> observerReference = new AtomicReference<>();
    final CountingProcedure observed = new CountingProcedure()
    {
      @Override
      public void call()
        throws Throwable
      {
        observerReference.get().reportStale();
      }
    };
    final ArezContext context = Arez.context();
    final Observer observer =
      new Observer( context,
                    null,
                    "MyObserver",
                    observed,
                    null,
                    Flags.READ_WRITE | Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );
    observerReference.set( observer );
    context.pauseScheduler();
    context.safeAction( observer::reportStale );

    assertTrue( observer.isScheduled() );

    final ReactionScheduler scheduler = context.getScheduler();
    scheduler.setMaxReactionRounds( 20 );

    context.markSchedulerAsActive();

    final AtomicInteger reactionCount = new AtomicInteger();
    assertInvariantFailure( () -> {
      while ( scheduler.runObserver() )
      {
        reactionCount.incrementAndGet();
      }
    }, "Arez-0101: Runaway reaction(s) detected. Observers still running after 20 rounds. " +
       "Current observers include: [" + observer.getName() + "]" );

    assertEquals( reactionCount.get(), 20 );

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertFalse( scheduler.isReactionsRunning() );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
  }

  @Test( timeOut = 5000L )
  public void runObserver_RunawayReactionsDetected_invariantChecksDisabled()
    throws Exception
  {
    ArezTestUtil.purgeReactionsWhenRunawayDetected();
    BrainCheckTestUtil.setCheckInvariants( false );

    final ArezContext context = Arez.context();

    final AtomicReference<Observer> observerReference = new AtomicReference<>();
    final CountAndObserveProcedure observed = new CountAndObserveProcedure()
    {
      @Override
      public void call()
        throws Throwable
      {
        observerReference.get().reportStale();
      }
    };
    final Observer observer =
      new Observer( context,
                    null,
                    ValueUtil.randomString(),
                    observed,
                    null,
                    Flags.RUN_LATER );

    observerReference.set( observer );
    context.pauseScheduler();
    context.safeAction( observer::reportStale );
    assertTrue( observer.isScheduled() );

    final ReactionScheduler scheduler = context.getScheduler();
    scheduler.setMaxReactionRounds( 20 );

    context.markSchedulerAsActive();

    final AtomicInteger reactionCount = new AtomicInteger();
    while ( scheduler.runObserver() )
    {
      reactionCount.incrementAndGet();
    }

    assertEquals( reactionCount.get(), 20 );

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertFalse( scheduler.isReactionsRunning() );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
  }

  @Test
  public void runPendingTasks()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = Arez.context().getScheduler();

    setupReadWriteTransaction();

    final Disposable[] disposables = new Disposable[ 3 ];
    for ( int i = 0; i < disposables.length; i++ )
    {
      disposables[ i ] = context.observable();
      scheduler.scheduleDispose( disposables[ i ] );
    }

    final Observer[] observers = new Observer[ 10 ];
    final ObservableValue[] observableValues = new ObservableValue[ observers.length ];
    final CountingProcedure[] observeds = new CountingProcedure[ observers.length ];
    for ( int i = 0; i < observers.length; i++ )
    {
      final int currentIndex = i;
      if ( i != 0 && 0 == i % 2 )
      {
        observeds[ i ] = new CountingProcedure()
        {
          @Override
          public void call()
            throws Throwable
          {
            super.call();
            if ( ( currentIndex == 8 && getCallCount() <= 2 ) || getCallCount() <= 1 )
            {
              observers[ currentIndex ].reportStale();
            }
          }
        };
      }
      else
      {
        observeds[ i ] = new CountingProcedure();
      }
      observers[ i ] =
        new Observer( Arez.context(),
                      null,
                      ValueUtil.randomString(),
                      observeds[ i ],
                      null,
                      Flags.READ_WRITE | Flags.AREZ_OR_EXTERNAL_DEPENDENCIES | Flags.RUN_LATER );
      observableValues[ i ] = context.observable();

      observers[ i ].setState( Flags.STATE_UP_TO_DATE );
      observableValues[ i ].rawAddObserver( observers[ i ] );
      observers[ i ].getDependencies().add( observableValues[ i ] );

      //observer has observed so setStale should result in reschedule
      observers[ i ].setState( Flags.STATE_STALE );
    }

    Transaction.setTransaction( null );

    assertEquals( scheduler.getPendingObservers().size(), observers.length );
    assertEquals( scheduler.getPendingDisposes().size(), disposables.length );

    scheduler.runPendingTasks();

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertFalse( scheduler.isReactionsRunning() );
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
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    setupReadWriteTransaction();

    final Observer[] observers = new Observer[ 10 ];
    final ObservableValue[] observableValues = new ObservableValue[ observers.length ];
    final CountingProcedure[] observeds = new CountingProcedure[ observers.length ];
    for ( int i = 0; i < observers.length; i++ )
    {
      final int currentIndex = i;
      if ( i != 0 && 0 == i % 2 )
      {
        observeds[ i ] = new CountingProcedure()
        {
          @Override
          public void call()
            throws Throwable
          {
            super.call();
            if ( ( currentIndex == 8 && getCallCount() <= 2 ) || getCallCount() <= 1 )
            {
              observers[ currentIndex ].reportStale();
            }
          }
        };
      }
      else
      {
        observeds[ i ] = new CountingProcedure();
      }
      observers[ i ] =
        new Observer( context,
                      null,
                      ValueUtil.randomString(),
                      observeds[ i ],
                      null,
                      Flags.READ_WRITE | Flags.AREZ_OR_EXTERNAL_DEPENDENCIES | Flags.RUN_LATER );
      observableValues[ i ] = context.observable();

      observers[ i ].setState( Flags.STATE_UP_TO_DATE );
      observableValues[ i ].rawAddObserver( observers[ i ] );
      observers[ i ].getDependencies().add( observableValues[ i ] );

      //observer has observed so setStale should result in reschedule
      observers[ i ].setState( Flags.STATE_STALE );
    }

    Transaction.setTransaction( null );

    assertEquals( scheduler.getPendingObservers().size(), observers.length );

    scheduler.runPendingTasks();

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertFalse( scheduler.isReactionsRunning() );
    assertEquals( scheduler.getPendingObservers().size(), 0 );
  }

  @Test
  public void runPendingTasks_onlyDisposables()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Disposable[] disposables = new Disposable[ 3 ];
    for ( int i = 0; i < disposables.length; i++ )
    {
      disposables[ i ] = context.observable();
      scheduler.scheduleDispose( disposables[ i ] );
    }

    assertEquals( scheduler.getPendingDisposes().size(), disposables.length );

    scheduler.runPendingTasks();

    assertEquals( scheduler.getRemainingReactionsInCurrentRound(), 0 );
    assertEquals( scheduler.getCurrentReactionRound(), 0 );
    assertFalse( scheduler.isReactionsRunning() );
    assertEquals( scheduler.getPendingObservers().size(), 0 );

    for ( final Disposable disposable : disposables )
    {
      assertTrue( Disposable.isDisposed( disposable ) );
    }
  }
}
