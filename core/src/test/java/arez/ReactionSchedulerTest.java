package arez;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    assertEquals( scheduler.getContext(), context );

    assertEquals( getNormalPriorityTaskCount( context ), 0 );
    assertEquals( scheduler.getPendingDisposes().size(), 0 );

    assertFalse( context.getExecutor().areTasksExecuting() );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertEquals( context.getExecutor().getMaxRounds(), 100 );
  }

  @Test
  public void construction_Zones_Disabled()
  {
    ArezTestUtil.disableZones();
    ArezTestUtil.resetState();

    assertInvariantFailure( () -> new ReactionScheduler( Arez.context() ),
                            "Arez-0164: ReactionScheduler passed a context but Arez.areZonesEnabled() is false" );

  }

  @Test
  public void onRunawayReactionsDetected()
  {
    ArezTestUtil.purgeReactionsWhenRunawayDetected();

    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountAndObserveProcedure() );
    context.getTaskQueue()
      .getTasksByPriority( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) )
      .add( observer.getTask() );
    observer.getTask().markAsScheduled();

    assertInvariantFailure( () -> context.getExecutor().onRunawayTasksDetected(),
                            "Arez-0101: Runaway task(s) detected. Tasks still running after " +
                            context.getExecutor().getMaxRounds() + " rounds. Current tasks include: [" +
                            observer.getName() + "]" );

    // Ensure observers purged
    assertEquals( getNormalPriorityTaskCount( context ), 0 );
  }

  @Test
  public void onRunawayReactionsDetected_noPurgeCOnfigured()
  {
    ArezTestUtil.noPurgeReactionsWhenRunawayDetected();

    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountAndObserveProcedure() );
    context.getTaskQueue()
      .getTasksByPriority( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) )
      .add( observer.getTask() );

    assertThrows( IllegalStateException.class, () -> context.getExecutor().onRunawayTasksDetected() );

    // Ensure observers not purged
    assertEquals( getNormalPriorityTaskCount( context ), 1 );
  }

  @Test
  public void onRunawayReactionsDetected_invariantCheckingDisabled()
  {
    BrainCheckTestUtil.setCheckInvariants( false );

    final ArezContext context = Arez.context();

    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );
    context.getTaskQueue()
      .getTasksByPriority( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) )
      .add( observer.getTask() );

    context.getExecutor().onRunawayTasksDetected();
  }

  @Test
  public void scheduleDispose()
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( getNormalPriorityTaskCount( context ), 0 );

    scheduler.scheduleDispose( observer );

    assertEquals( scheduler.getPendingDisposes().size(), 1 );
    assertTrue( scheduler.getPendingDisposes().contains( observer ) );
  }

  @Test
  public void scheduleDispose_alreadyScheduled()
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( getNormalPriorityTaskCount( context ), 0 );

    scheduler.scheduleDispose( observer );

    scheduler.getPendingDisposes().add( observer );

    assertInvariantFailure( () -> scheduler.scheduleDispose( observer ),
                            "Arez-0165: Attempting to schedule disposable '" + observer.getName() +
                            "' when disposable is already pending." );
  }

  @Test
  public void runDispose()
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
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( getNormalPriorityTaskCount( context ), 0 );
    assertFalse( scheduler.hasTasksToSchedule() );

    queueTask( context, observer );

    assertEquals( getNormalPriorityTaskCount( context ), 1 );
    assertTrue( context.getTaskQueue()
                  .getTasksByPriority( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) )
                  .contains( observer.getTask() ) );
    assertTrue( scheduler.hasTasksToSchedule() );
  }

  private void queueTask( @Nonnull final ArezContext context, @Nonnull final Observer observer )
  {
    context.getTaskQueue().queueTask( observer.getPriorityIndex(), observer.getTask() );
  }

  @Test
  public void scheduleReaction_highPriorityObserver()
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

    assertEquals( getNormalPriorityTaskCount( context ), 0 );
    assertFalse( scheduler.hasTasksToSchedule() );

    queueTask( context, observer0 );
    queueTask( context, observer1 );
    queueTask( context, observer2 );
    queueTask( context, observer3 );
    queueTask( context, observer4 );

    assertTrue( scheduler.hasTasksToSchedule() );

    final CircularBuffer<Task> tasksByPriority = getTasksByPriority( context, Flags.PRIORITY_HIGHEST );
    assertEquals( tasksByPriority.size(), 1 );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_HIGH ).size(), 2 );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_NORMAL ).size(), 2 );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_LOW ).size(), 0 );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_LOWEST ).size(), 0 );

    assertEquals( getTasksByPriority( context, Flags.PRIORITY_HIGHEST ).get( 0 ), observer0.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_HIGH ).get( 0 ), observer2.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_HIGH ).get( 1 ), observer4.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_NORMAL ).get( 0 ), observer1.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_NORMAL ).get( 1 ), observer3.getTask() );
  }

  @Test
  public void scheduleReaction_multipleObserverPriorities()
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

    assertEquals( getNormalPriorityTaskCount( context ), 0 );
    assertFalse( scheduler.hasTasksToSchedule() );

    queueTask( context, observer0 );
    queueTask( context, observer1 );
    queueTask( context, observer2 );
    queueTask( context, observer3 );
    queueTask( context, observer4 );
    queueTask( context, observer5 );
    queueTask( context, observer6 );
    queueTask( context, observer7 );
    queueTask( context, observer8 );

    assertTrue( scheduler.hasTasksToSchedule() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_HIGHEST ).size(), 1 );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_HIGH ).size(), 2 );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_NORMAL ).size(), 2 );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_LOW ).size(), 2 );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_LOWEST ).size(), 2 );

    assertEquals( getTasksByPriority( context, Flags.PRIORITY_HIGHEST ).get( 0 ), observer0.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_HIGH ).get( 1 ), observer6.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_HIGH ).get( 1 ), observer6.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_NORMAL ).get( 0 ), observer3.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_NORMAL ).get( 1 ), observer7.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_LOW ).get( 0 ), observer4.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_LOW ).get( 1 ), observer8.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_LOWEST ).get( 0 ), observer1.getTask() );
    assertEquals( getTasksByPriority( context, Flags.PRIORITY_LOWEST ).get( 1 ), observer5.getTask() );
  }

  @Nonnull
  private CircularBuffer<Task> getTasksByPriority( @Nonnull final ArezContext context, final int priority )
  {
    final int priorityIndex = Flags.getPriorityIndex( priority );
    return context.getTaskQueue().getTasksByPriority( priorityIndex );
  }

  @Test
  public void scheduleReaction_observerAlreadyScheduled()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    context
      .getTaskQueue()
      .getTasksByPriority( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) )
      .add( observer.getTask() );

    assertInvariantFailure( () -> queueTask( context, observer ),
                            "Arez-0099: Attempting to queue task named '" + observer.getName() +
                            "' when task is already queued." );
  }

  @Test
  public void runObserver_singlePendingObserver()
  {
    final ArezContext context = Arez.context();
    context.markSchedulerAsActive();

    final Observer observer = newReadWriteObserver( context );
    final CountingProcedure observed = (CountingProcedure) observer.getObserve();
    assertNotNull( observed );

    final CountingProcedure onDepsChange = (CountingProcedure) observer.getOnDepsChange();
    assertNull( onDepsChange );

    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );
    //observer has observed so setStale should result in reschedule
    observer.setState( Flags.STATE_STALE );

    Transaction.setTransaction( null );

    assertTrue( observer.getTask().isScheduled() );
    assertEquals( getNormalPriorityTaskCount( context ), 1 );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );

    final boolean ran = context.getExecutor().runNextTask();

    assertTrue( ran );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( getNormalPriorityTaskCount( context ), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 1 );
    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertFalse( observer.getTask().isScheduled() );

    final boolean ran2 = context.getExecutor().runNextTask();

    assertFalse( ran2 );
    assertEquals( observed.getCallCount(), 1 );
    assertEquals( getNormalPriorityTaskCount( context ), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertFalse( observer.getTask().isScheduled() );
  }

  @Test
  public void runObserver_insideTransaction()
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = newReadWriteObserver( context );
    final CountingProcedure observed = (CountingProcedure) observer.getObserve();
    assertNotNull( observed );

    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_UP_TO_DATE );
    //observer has observed so setStale should result in reschedule
    observer.setState( Flags.STATE_STALE );

    assertTrue( observer.getTask().isScheduled() );
    assertEquals( getNormalPriorityTaskCount( context ), 1 );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );

    assertInvariantFailure( scheduler::runPendingTasks,
                            "Arez-0100: Invoked runPendingTasks() when transaction named '" +
                            context.getTransaction().getName() + "' is active." );
  }

  @Test
  public void runObserver_multiplePendingObservers()
  {
    final ArezContext context = Arez.context();
    context.markSchedulerAsActive();

    setupReadWriteTransaction();
    // Purge the observer that was scheduled
    context.getTaskQueue().getTasksByPriority( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) ).clear();

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
      assertTrue( observers[ i ].getTask().isScheduled() );
    }

    Transaction.setTransaction( null );

    assertEquals( getNormalPriorityTaskCount( context ), observers.length );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );

    // Start from last observer and go down to first
    for ( int i = 0; i < round1Size; i++ )
    {
      assertTrue( context.getExecutor().runNextTask() );
      assertEquals( context.getExecutor().getCurrentRound(), 1 );
    }

    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 1 );
    assertEquals( getNormalPriorityTaskCount( context ), round2Size );
    assertTrue( context.getExecutor().areTasksExecuting() );

    for ( int i = 0; i < round2Size; i++ )
    {
      assertTrue( context.getExecutor().runNextTask() );
      assertEquals( context.getExecutor().getCurrentRound(), 2 );
    }

    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 2 );
    assertEquals( getNormalPriorityTaskCount( context ), round3Size );
    assertTrue( context.getExecutor().areTasksExecuting() );

    for ( int i = 0; i < round3Size; i++ )
    {
      assertTrue( context.getExecutor().runNextTask() );
      assertEquals( context.getExecutor().getCurrentRound(), 3 );
    }

    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 3 );
    assertEquals( getNormalPriorityTaskCount( context ), 0 );
    assertTrue( context.getExecutor().areTasksExecuting() );

    assertFalse( context.getExecutor().runNextTask() );

    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertFalse( context.getExecutor().areTasksExecuting() );
    assertEquals( getNormalPriorityTaskCount( context ), 0 );

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
  {
    ArezTestUtil.purgeReactionsWhenRunawayDetected();
    setIgnoreObserverErrors( true );

    final AtomicReference<Observer> observerReference = new AtomicReference<>();
    final CountingProcedure observed = new CountingProcedure()
    {
      @Override
      public void call()
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

    assertTrue( observer.getTask().isScheduled() );

    context.markSchedulerAsActive();

    final AtomicInteger reactionCount = new AtomicInteger();
    assertInvariantFailure( () -> {
                              while ( context.getExecutor().runNextTask() )
                              {
                                reactionCount.incrementAndGet();
                              }
                            },
                            "Arez-0101: Runaway task(s) detected. Tasks still running after 100 rounds. Current tasks include: [MyObserver]" );

    assertEquals( reactionCount.get(), 100 );

    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertFalse( context.getExecutor().areTasksExecuting() );
    assertEquals( getNormalPriorityTaskCount( context ), 0 );
  }

  @Test( timeOut = 5000L )
  public void runObserver_RunawayReactionsDetected_invariantChecksDisabled()
  {
    ArezTestUtil.purgeReactionsWhenRunawayDetected();
    BrainCheckTestUtil.setCheckInvariants( false );

    final ArezContext context = Arez.context();

    final AtomicReference<Observer> observerReference = new AtomicReference<>();
    final CountAndObserveProcedure observed = new CountAndObserveProcedure()
    {
      @Override
      public void call()
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
    assertTrue( observer.getTask().isScheduled() );

    context.markSchedulerAsActive();

    final AtomicInteger reactionCount = new AtomicInteger();
    while ( context.getExecutor().runNextTask() )
    {
      reactionCount.incrementAndGet();
    }

    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertFalse( context.getExecutor().areTasksExecuting() );
    assertEquals( getNormalPriorityTaskCount( context ), 0 );
  }

  @Test
  public void runPendingTasks()
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

    assertEquals( getNormalPriorityTaskCount( context ), observers.length );
    assertEquals( scheduler.getPendingDisposes().size(), disposables.length );

    scheduler.runPendingTasks();

    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertFalse( context.getExecutor().areTasksExecuting() );
    assertEquals( getNormalPriorityTaskCount( context ), 0 );
    assertEquals( scheduler.getPendingDisposes().size(), 0 );

    for ( final Disposable disposable : disposables )
    {
      assertTrue( Disposable.isDisposed( disposable ) );
    }
  }

  private int getNormalPriorityTaskCount( final ArezContext context )
  {
    return context.getTaskQueue().getTasksByPriority( Flags.getPriorityIndex( Flags.PRIORITY_NORMAL ) ).size();
  }

  @Test
  public void runPendingTasks_onlyReactions()
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

    assertEquals( getNormalPriorityTaskCount( context ), observers.length );

    scheduler.runPendingTasks();

    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertFalse( context.getExecutor().areTasksExecuting() );
    assertEquals( getNormalPriorityTaskCount( context ), 0 );
  }

  @Test
  public void runPendingTasks_onlyDisposables()
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

    assertEquals( context.getExecutor().getRemainingTasksInCurrentRound(), 0 );
    assertEquals( context.getExecutor().getCurrentRound(), 0 );
    assertFalse( context.getExecutor().areTasksExecuting() );
    assertEquals( getNormalPriorityTaskCount( context ), 0 );

    for ( final Disposable disposable : disposables )
    {
      assertTrue( Disposable.isDisposed( disposable ) );
    }
  }
}
