package arez;

import arez.component.TypeBasedLocator;
import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComponentCreateStartedEvent;
import arez.spy.ComputedValueCreatedEvent;
import arez.spy.ObservableValueCreatedEvent;
import arez.spy.ObserverCreatedEvent;
import arez.spy.ObserverErrorEvent;
import arez.spy.ObserverInfo;
import arez.spy.Priority;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
import arez.spy.ReactionCompletedEvent;
import arez.spy.ReactionScheduledEvent;
import arez.spy.ReactionStartedEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class ArezContextTest
  extends AbstractArezTest
{
  @Test
  public void generateName()
  {
    final ArezContext context = Arez.context();

    // Use passed in name
    assertEquals( context.generateName( "ComputedValue", "MyName" ), "MyName" );

    //synthesize name
    context.setNextNodeId( 1 );
    assertEquals( context.generateName( "ComputedValue", null ), "ComputedValue@1" );
    assertEquals( context.getNextNodeId(), 2 );

    ArezTestUtil.disableNames();

    //Ignore name
    assertEquals( context.generateName( "ComputedValue", "MyName" ), null );

    //Null name also fine
    assertEquals( context.generateName( "ComputedValue", null ), null );
  }

  @Test
  public void triggerScheduler()
  {
    final ArezContext context = Arez.context();
    final AtomicInteger callCount = new AtomicInteger();

    context.observer( () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, Flags.RUN_LATER );

    assertEquals( callCount.get(), 0 );

    context.triggerScheduler();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void triggerScheduler_alreadyActive()
  {
    final ArezContext context = Arez.context();
    final AtomicInteger callCount = new AtomicInteger();

    context.observer( () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, Flags.RUN_LATER );

    assertEquals( callCount.get(), 0 );

    context.markSchedulerAsActive();

    context.triggerScheduler();

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void triggerScheduler_inEnvironment()
  {
    final ArezContext context = Arez.context();
    final AtomicInteger callCount = new AtomicInteger();
    final AtomicReference<String> environment = new AtomicReference<>();

    context.setEnvironment( a -> {
      environment.set( "RED" );
      a.call();
      environment.set( null );
    } );

    context.observer( () -> {
      observeADependency();
      callCount.incrementAndGet();
      assertEquals( environment.get(), "RED" );
    }, Flags.RUN_LATER );

    assertEquals( callCount.get(), 0 );
    assertEquals( environment.get(), null );

    context.triggerScheduler();

    assertEquals( callCount.get(), 1 );
    assertEquals( environment.get(), null );
  }

  @Test
  public void triggerScheduler_inEnvironment_whereEnvironmentSchedulesActions()
  {
    final ArezContext context = Arez.context();
    final AtomicInteger callCount = new AtomicInteger();
    final AtomicReference<String> environment = new AtomicReference<>();

    final AtomicInteger count = new AtomicInteger( 3 );
    final AtomicReference<Observer> observerReference = new AtomicReference<>();
    context.setEnvironment( a -> {
      environment.set( "RED" );
      a.call();
      /*
       * This simulates the scenario where something like react4j has only scheduler that will
       * react to changes in arez and potentially re-schedule arez events.
       */
      if ( count.decrementAndGet() > 0 )
      {
        context.safeAction( () -> observerReference.get().setState( Flags.STATE_STALE ),
                            Flags.NO_VERIFY_ACTION_REQUIRED );
      }
      environment.set( null );
    } );

    final Observer observer =
      context.observer( () -> {
        final ObservableValue<Object> observableValue = Arez.context().observable();
        observableValue.reportObserved();
        callCount.incrementAndGet();
        assertEquals( environment.get(), "RED" );

      }, Flags.RUN_LATER );

    observerReference.set( observer );

    assertEquals( callCount.get(), 0 );
    assertEquals( environment.get(), null );

    context.triggerScheduler();

    assertEquals( callCount.get(), 3 );
    assertEquals( count.get(), 0 );
    assertEquals( environment.get(), null );
  }

  @Test
  public void isReadOnlyTransactionActive()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isReadOnlyTransactionActive() );

    context.action( () -> {
      assertTrue( context.isTransactionActive() );
      assertFalse( context.isReadOnlyTransactionActive() );
      observeADependency();
      context.action( () -> {
        assertTrue( context.isTransactionActive() );
        assertTrue( context.isReadOnlyTransactionActive() );
        observeADependency();
      }, Flags.READ_ONLY );
    } );

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isReadOnlyTransactionActive() );
  }

  @Test
  public void isWriteTransactionActive()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isReadWriteTransactionActive() );

    context.action( () -> {
      assertTrue( context.isTransactionActive() );
      assertTrue( context.isReadWriteTransactionActive() );
      observeADependency();
      context.action( () -> {
        assertTrue( context.isTransactionActive() );
        assertFalse( context.isReadWriteTransactionActive() );
        observeADependency();
      }, Flags.READ_ONLY );
    } );

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isReadWriteTransactionActive() );
  }

  @Test
  public void isTrackingTransactionActive()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isReadOnlyTransactionActive() );
    assertFalse( context.isReadWriteTransactionActive() );
    assertFalse( context.isTrackingTransactionActive() );

    context.action( () -> {
      assertTrue( context.isTransactionActive() );
      assertFalse( context.isReadOnlyTransactionActive() );
      assertTrue( context.isReadWriteTransactionActive() );
      observeADependency();
    } );

    final Observer tracker = context.tracker( () -> assertFalse( context.isTrackingTransactionActive() ) );

    context.observe( tracker, () -> {
      assertTrue( context.isTransactionActive() );
      assertTrue( context.isReadOnlyTransactionActive() );
      assertFalse( context.isReadWriteTransactionActive() );
      assertTrue( context.isTrackingTransactionActive() );
    } );

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isReadOnlyTransactionActive() );
    assertFalse( context.isReadWriteTransactionActive() );
    assertFalse( context.isTrackingTransactionActive() );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void requireNewTransaction_false()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    context.action( () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();

      context.action( () -> assertNotEquals( context.getTransaction(), transaction ),
                      Flags.NO_VERIFY_ACTION_REQUIRED | Flags.REQUIRE_NEW_TRANSACTION );

      final int result1 = context.action( () -> {
        assertNotEquals( context.getTransaction(), transaction );
        return 0;
      }, Flags.NO_VERIFY_ACTION_REQUIRED | Flags.REQUIRE_NEW_TRANSACTION );

      context.safeAction( () -> assertNotEquals( context.getTransaction(), transaction ),
                          Flags.NO_VERIFY_ACTION_REQUIRED | Flags.REQUIRE_NEW_TRANSACTION );

      final int result2 =
        context.safeAction( () -> {
          assertNotEquals( context.getTransaction(), transaction );
          return 0;
        }, Flags.NO_VERIFY_ACTION_REQUIRED | Flags.REQUIRE_NEW_TRANSACTION );

      context.action( () -> assertEquals( context.getTransaction(), transaction ), Flags.NO_VERIFY_ACTION_REQUIRED );

      final int result3 = context.action( () -> {
        assertEquals( context.getTransaction(), transaction );
        return 0;
      }, Flags.NO_VERIFY_ACTION_REQUIRED );

      context.safeAction( () -> assertEquals( context.getTransaction(), transaction ),
                          Flags.NO_VERIFY_ACTION_REQUIRED );

      final int result4 =
        context.safeAction( () -> {
          assertEquals( context.getTransaction(), transaction );
          return 0;
        }, Flags.NO_VERIFY_ACTION_REQUIRED );
    }, Flags.NO_VERIFY_ACTION_REQUIRED );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void nestedAction_allowed()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger updateCalled = new AtomicInteger();

    final Observer tracker =
      context.tracker( updateCalled::incrementAndGet, Flags.READ_WRITE | Flags.NESTED_ACTIONS_ALLOWED );

    context.observe( tracker, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();

      context.action( () -> assertNotEquals( context.getTransaction(), transaction ),
                      Flags.NO_VERIFY_ACTION_REQUIRED | Flags.REQUIRE_NEW_TRANSACTION );

      final int result1 = context.action( () -> {
        assertNotEquals( context.getTransaction(), transaction );
        return 0;
      }, Flags.NO_VERIFY_ACTION_REQUIRED | Flags.REQUIRE_NEW_TRANSACTION );

      context.safeAction( () -> assertNotEquals( context.getTransaction(), transaction ),
                          Flags.NO_VERIFY_ACTION_REQUIRED | Flags.REQUIRE_NEW_TRANSACTION );

      final int result2 = context.safeAction( () -> {
        assertNotEquals( context.getTransaction(), transaction );
        return 0;
      }, Flags.NO_VERIFY_ACTION_REQUIRED | Flags.REQUIRE_NEW_TRANSACTION );

      context.action( () -> assertEquals( context.getTransaction(), transaction ), Flags.NO_VERIFY_ACTION_REQUIRED );
    } );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void nestedAction_notAllowed()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger updateCalled = new AtomicInteger();

    final Observer tracker =
      context.tracker( updateCalled::incrementAndGet, Flags.READ_WRITE | Flags.NESTED_ACTIONS_DISALLOWED );

    context.observe( tracker, () -> {

      assertInvariantFailure( () -> context.action( "A1",
                                                    AbstractArezTest::observeADependency,
                                                    Flags.NO_VERIFY_ACTION_REQUIRED | Flags.REQUIRE_NEW_TRANSACTION ),
                              "Arez-0187: Attempting to nest action named 'A1' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );

      assertInvariantFailure( () -> context.action( "A2",
                                                    () -> 1,
                                                    Flags.NO_VERIFY_ACTION_REQUIRED | Flags.REQUIRE_NEW_TRANSACTION ),
                              "Arez-0187: Attempting to nest action named 'A2' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );

      assertInvariantFailure( () -> context.safeAction( "A3",
                                                        AbstractArezTest::observeADependency,
                                                        Flags.NO_VERIFY_ACTION_REQUIRED |
                                                        Flags.REQUIRE_NEW_TRANSACTION ),
                              "Arez-0187: Attempting to nest action named 'A3' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );

      assertInvariantFailure( () -> context.safeAction( "A4",
                                                        () -> 1,
                                                        Flags.NO_VERIFY_ACTION_REQUIRED |
                                                        Flags.REQUIRE_NEW_TRANSACTION ),
                              "Arez-0187: Attempting to nest action named 'A4' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );
    } );
  }

  @Test
  public void verifyActionFlags()
    throws Throwable
  {
    final Procedure executable = () -> {
    };
    assertInvariantFailure( () -> Arez.context().action( executable, Flags.DEACTIVATE_ON_UNOBSERVE ),
                            "Arez-0212: Flags passed to action 'Action@1' include some " +
                            "unexpected flags set: " + Flags.DEACTIVATE_ON_UNOBSERVE );

  }

  @Test
  public void action_function()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertInvariantFailure( context::getTransaction,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final String expectedValue = ValueUtil.randomString();

    final ObservableValue<?> observableValue = context.observable();
    assertEquals( observableValue.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String v0 =
      context.action( name, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), name );
        assertEquals( transaction.getPrevious(), null );
        assertEquals( transaction.getContext(), context );
        assertEquals( transaction.getId(), nextNodeId );
        assertEquals( transaction.isMutation(), false );

        assertEquals( observableValue.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

        observableValue.reportObserved();

        //Not tracking so no state updated
        assertEquals( observableValue.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

        return expectedValue;
      }, Flags.READ_ONLY, new Object[]{ param1, param2, param3 } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    //ObservableValue still not updated
    assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    assertEquals( observableValue.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), null );
      assertEquals( e.returnsResult(), true );
      assertEquals( e.getResult(), v0 );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void action_function_throwsException()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertInvariantFailure( context::getTransaction,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final String name = ValueUtil.randomString();

    final IOException ioException = new IOException();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    assertThrows( IOException.class, () ->
      context.action( name, () -> {
        throw ioException;
      }, 0, new Object[]{ param1, param2, param3 } ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), true );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), true );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), ioException );
      assertEquals( e.returnsResult(), true );
      assertEquals( e.getResult(), null );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void action_function_NameButNoMutationVariant()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    context.action( name, () -> {
      observeADependency();
      assertEquals( context.getTransaction().isMutation(), true );
      assertEquals( context.getTransaction().getName(), name );
      return ValueUtil.randomString();
    } );
  }

  @Test
  public void action_procedure_verifyActionRequired_false()
    throws Throwable
  {
    final Procedure executable = ValueUtil::randomString;
    Arez.context().action( executable, Flags.NO_VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_procedure_verifyActionRequired_true_butInvariantsDisabled()
    throws Throwable
  {
    ArezTestUtil.noCheckInvariants();

    final Procedure executable = ValueUtil::randomString;
    Arez.context().action( executable, Flags.VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_procedure_verifyActionRequired_true()
    throws Throwable
  {
    final Procedure procedure = ValueUtil::randomString;
    assertInvariantFailure( () -> Arez.context().action( "X", procedure, Flags.VERIFY_ACTION_REQUIRED ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void action_procedure_verifyActionRequired_true_is_default()
    throws Throwable
  {
    assertInvariantFailure( () -> Arez.context().action( "X", (Procedure) ValueUtil::randomString ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void action_function_verifyActionRequired_false()
    throws Throwable
  {
    Arez.context().action( (Function<String>) ValueUtil::randomString, Flags.NO_VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_function_verifyActionRequired_true_butInvariantsDisabled()
    throws Throwable
  {
    ArezTestUtil.noCheckInvariants();

    Arez.context().action( (Function<String>) ValueUtil::randomString, Flags.VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_function_verifyActionRequired_true()
    throws Throwable
  {
    final Function<String> function = ValueUtil::randomString;
    assertInvariantFailure( () -> Arez.context().action( "X", function, Flags.VERIFY_ACTION_REQUIRED ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void action_function_verifyActionRequired_true_is_default()
    throws Throwable
  {
    final Function<String> function = ValueUtil::randomString;
    assertInvariantFailure( () -> Arez.context().action( "X", function ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_false()
    throws Throwable
  {
    final SafeProcedure procedure = ValueUtil::randomString;
    Arez.context().safeAction( ValueUtil.randomString(), procedure, Flags.NO_VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_true_butInvariantsDisabled()
    throws Throwable
  {
    ArezTestUtil.noCheckInvariants();

    final SafeProcedure executable = ValueUtil::randomString;
    Arez.context().safeAction( ValueUtil.randomString(), executable, Flags.VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_true()
    throws Throwable
  {
    final SafeProcedure procedure = ValueUtil::randomString;
    assertInvariantFailure( () -> Arez.context().safeAction( "X", procedure, Flags.VERIFY_ACTION_REQUIRED ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_true_is_default()
    throws Throwable
  {
    assertInvariantFailure( () -> Arez.context().safeAction( "X", (SafeProcedure) ValueUtil::randomString ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void safeAction_function_verifyActionRequired_false()
    throws Throwable
  {
    Arez.context().safeAction( (SafeFunction<String>) ValueUtil::randomString, Flags.NO_VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_function_verifyActionRequired_true_butInvariantsDisabled()
    throws Throwable
  {
    ArezTestUtil.noCheckInvariants();

    Arez.context().safeAction( (SafeFunction<String>) ValueUtil::randomString, Flags.VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_function_verifyActionRequired_true()
    throws Throwable
  {
    final SafeFunction<String> function = ValueUtil::randomString;
    assertInvariantFailure( () -> Arez.context().safeAction( "X", function, Flags.VERIFY_ACTION_REQUIRED ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void safeAction_function_verifyActionRequired_true_is_default()
    throws Throwable
  {
    assertInvariantFailure( () -> Arez.context().safeAction( "X", (SafeFunction<String>) ValueUtil::randomString ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void action_function_minimalParameters()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();

    final ObservableValue<Object> observableValue = context.observable();
    final int nextNodeId = context.getNextNodeId();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String v0 =
      context.action( () -> {
        observableValue.reportObserved();
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), "Action@" + nextNodeId );
        assertEquals( transaction.isMutation(), true );

        return expectedValue;
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartedEvent.class, e -> {
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 0 );
    } );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> {
      assertEquals( e.isMutation(), true );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> {
      assertEquals( e.isMutation(), true );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> {
      assertEquals( e.getThrowable(), null );
      assertEquals( e.returnsResult(), true );
      assertEquals( e.getResult(), v0 );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 0 );
    } );
  }

  @Test
  public void track_function()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer tracker = context.tracker( callCount::incrementAndGet, Flags.READ_WRITE );

    final ObservableValue<?> observableValue = context.observable();
    assertEquals( observableValue.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String v0 =
      context.observe( tracker, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), tracker.getName() );
        assertEquals( transaction.isMutation(), tracker.isMutation() );

        assertEquals( observableValue.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

        observableValue.reportObserved();

        // Tracking so state updated
        final ArrayList<ObservableValue<?>> observableValues = transaction.getObservableValues();
        assertNotNull( observableValues );
        assertEquals( observableValues.size(), 1 );
        assertEquals( observableValue.getObservers().size(), 0 );
        assertEquals( observableValue.getLastTrackerTransactionId(), nextNodeId );

        return expectedValue;
      }, new Object[]{ param1, param2, param3 } );

    assertFalse( context.isTransactionActive() );
    context.getSpy().removeSpyEventHandler( handler );

    assertEquals( v0, expectedValue );

    assertEquals( observableValue.getLastTrackerTransactionId(), 0 );
    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );

    // Reaction not called as the function sets up initial tracking
    assertEquals( callCount.get(), 0 );

    context.action( observableValue::reportChanged );

    assertEquals( callCount.get(), 1 );
    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartedEvent.class, e -> {
      assertEquals( e.isTracked(), true );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> {
      assertEquals( e.isMutation(), true );
      final ObserverInfo info = e.getTracker();
      assertNotNull( info );
      assertEquals( info.getName(), tracker.getName() );
    } );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> {
      assertEquals( e.isMutation(), true );
      final ObserverInfo info = e.getTracker();
      assertNotNull( info );
      assertEquals( info.getName(), tracker.getName() );
    } );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> {
      assertEquals( e.getThrowable(), null );
      assertEquals( e.returnsResult(), true );
      assertEquals( e.getResult(), v0 );
      assertEquals( e.isTracked(), true );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void track_function_passingNonTracker()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertInvariantFailure( () -> context.observe( observer, callCount::incrementAndGet ),
                            "Arez-0017: Attempted to invoke observe(..) on observer named '" + observer.getName() +
                            "' but observer is not configured to use an application executor." );

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void action_safeFunction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertInvariantFailure( context::getTransaction,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final String expectedValue = ValueUtil.randomString();

    final ObservableValue<?> observableValue = context.observable();
    assertEquals( observableValue.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String v0 =
      context.safeAction( name, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), name );
        assertEquals( transaction.getPrevious(), null );
        assertEquals( transaction.getContext(), context );
        assertEquals( transaction.getId(), nextNodeId );
        assertEquals( transaction.isMutation(), false );

        assertEquals( observableValue.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

        observableValue.reportObserved();

        //Not tracking so no state updated
        assertEquals( observableValue.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

        return expectedValue;
      }, Flags.READ_ONLY, new Object[]{ param1, param2, param3 } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    //ObservableValue still not updated
    assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    assertEquals( observableValue.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), null );
      assertEquals( e.returnsResult(), true );
      assertEquals( e.getResult(), v0 );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void action_safeFunction_throws_Exception()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertInvariantFailure( context::getTransaction,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final AccessControlException secException = new AccessControlException( "" );

    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    assertThrows( AccessControlException.class, () ->
      context.safeAction( name, () -> {
        throw secException;
      }, Flags.READ_ONLY, new Object[]{ param1, param2, param3 } ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), secException );
      assertEquals( e.returnsResult(), true );
      assertEquals( e.getResult(), null );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void action_safeFunction_minimalParameters()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final int nextNodeId = context.currentNextTransactionId();

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.safeAction( () -> {
        observeADependency();
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), "Action@" + nextNodeId );
        assertEquals( transaction.isMutation(), true );
        return expectedValue;
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void action_safeFunction_NameButNoMutationVariant()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    context.safeAction( name, () -> {
      observeADependency();
      assertEquals( context.getTransaction().isMutation(), true );
      assertEquals( context.getTransaction().getName(), name );
      return ValueUtil.randomString();
    } );
  }

  @Test
  public void track_safeFunction()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer tracker = context.tracker( callCount::incrementAndGet );

    final ObservableValue<?> observableValue = context.observable();
    assertEquals( observableValue.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();

    final String v0 =
      context.safeObserve( tracker, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), tracker.getName() );
        assertEquals( transaction.isMutation(), tracker.isMutation() );

        assertEquals( observableValue.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

        observableValue.reportObserved();

        // Tracking so state updated
        final ArrayList<ObservableValue<?>> observableValues = transaction.getObservableValues();
        assertNotNull( observableValues );
        assertEquals( observableValues.size(), 1 );
        assertEquals( observableValue.getObservers().size(), 0 );
        assertEquals( observableValue.getLastTrackerTransactionId(), nextNodeId );

        return expectedValue;
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    assertEquals( observableValue.getLastTrackerTransactionId(), 0 );
    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );

    // Reaction not called as the function sets up initial tracking
    assertEquals( callCount.get(), 0 );

    context.action( observableValue::reportChanged );

    assertEquals( callCount.get(), 1 );
    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );
  }

  @Test
  public void track_safeFunction_passingNonTracker()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertInvariantFailure( () -> context.safeObserve( observer, callCount::incrementAndGet ),
                            "Arez-0018: Attempted to invoke safeObserve(..) on observer named '" +
                            observer.getName() + "' but observer is not configured to use an application executor." );

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void safeAction_safeProcedure_minimalParameters()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final int nextNodeId = context.getNextNodeId();
    context.safeAction( () -> {
      observeADependency();
      assertTrue( context.isTransactionActive() );
      assertEquals( context.getTransaction().isMutation(), true );
      assertEquals( context.getTransaction().getName(), "Action@" + nextNodeId );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void safeAction_safeProcedure_NameButNoMutationVariant()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    context.safeAction( name, () -> {
      observeADependency();
      assertEquals( context.getTransaction().isMutation(), true );
      assertEquals( context.getTransaction().getName(), name );
    } );
  }

  @Test
  public void action_safeProcedure_throws_Exception()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertInvariantFailure( context::getTransaction,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final AccessControlException secException = new AccessControlException( "" );

    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final SafeProcedure procedure = () -> {
      throw secException;
    };
    assertThrows( AccessControlException.class,
                  () -> context.safeAction( name, procedure, 0, new Object[]{ param1, param2, param3 } ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), true );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), true );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), secException );
      assertEquals( e.returnsResult(), false );
      assertEquals( e.getResult(), null );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void track_safeProcedure()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final AtomicInteger callCount = new AtomicInteger();

    final Observer tracker = context.tracker( callCount::incrementAndGet );

    final ObservableValue<?> observableValue = context.observable();
    assertEquals( observableValue.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();

    context.safeObserve( tracker, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), tracker.getName() );
      assertEquals( transaction.isMutation(), tracker.isMutation() );

      assertEquals( observableValue.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

      observableValue.reportObserved();

      // Tracking so state updated
      final ArrayList<ObservableValue<?>> observableValues = transaction.getObservableValues();
      assertNotNull( observableValues );
      assertEquals( observableValues.size(), 1 );
      assertEquals( observableValue.getObservers().size(), 0 );
      assertEquals( observableValue.getLastTrackerTransactionId(), nextNodeId );
    } );

    assertFalse( context.isTransactionActive() );

    assertEquals( observableValue.getLastTrackerTransactionId(), 0 );
    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );

    // Reaction not called as the function sets up initial tracking
    assertEquals( callCount.get(), 0 );

    context.action( observableValue::reportChanged );

    assertEquals( callCount.get(), 1 );
    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );
  }

  @Test
  public void track_safeProcedure_passingNonTracker()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final SafeProcedure procedure = callCount::incrementAndGet;
    assertInvariantFailure( () -> context.safeObserve( observer, procedure ),
                            "Arez-0020: Attempted to invoke safeObserve(..) on observer named '" +
                            observer.getName() + "' but observer is not configured to use an application executor." );

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void action_procedure_NameButNoMutationVariant()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    context.action( name, () -> {
      observeADependency();
      assertEquals( context.getTransaction().isMutation(), true );
      assertEquals( context.getTransaction().getName(), name );
    } );
  }

  @Test
  public void action_procedure_minimalParameters()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final int nextNodeId = context.currentNextTransactionId();
    context.action( () -> {
      observeADependency();
      assertTrue( context.isTransactionActive() );
      assertEquals( context.getTransaction().isMutation(), true );
      assertEquals( context.getTransaction().getName(), "Action@" + nextNodeId );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void track_procedure_passingNonTracker()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final Procedure procedure = callCount::incrementAndGet;
    assertInvariantFailure( () -> context.observe( observer, procedure ),
                            "Arez-0019: Attempted to invoke observe(..) on observer named '" +
                            observer.getName() + "' but observer is not configured to use an application executor." );

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void track_procedure()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final AtomicInteger callCount = new AtomicInteger();

    final Observer tracker = context.tracker( callCount::incrementAndGet );

    final ObservableValue<?> observableValue = context.observable();
    assertEquals( observableValue.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();

    context.observe( tracker, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), tracker.getName() );
      assertEquals( transaction.isMutation(), tracker.isMutation() );

      assertEquals( observableValue.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

      observableValue.reportObserved();

      // Tracking so state updated
      final ArrayList<ObservableValue<?>> observableValues = transaction.getObservableValues();
      assertNotNull( observableValues );
      assertEquals( observableValues.size(), 1 );
      assertEquals( observableValue.getObservers().size(), 0 );
      assertEquals( observableValue.getLastTrackerTransactionId(), nextNodeId );
    } );

    assertFalse( context.isTransactionActive() );

    assertEquals( observableValue.getLastTrackerTransactionId(), 0 );
    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );

    // Reaction not called as the function sets up initial tracking
    assertEquals( callCount.get(), 0 );

    context.action( observableValue::reportChanged );

    assertEquals( callCount.get(), 1 );
    assertEquals( observableValue.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );
  }

  @Test
  public void nonTrackingSafeProcedureObservingSingleObservable()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertInvariantFailure( context::getTransaction,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final ObservableValue<?> observableValue = context.observable();
    assertEquals( observableValue.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();
    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    context.safeAction( name, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), name );
      assertEquals( transaction.getPrevious(), null );
      assertEquals( transaction.getContext(), context );
      assertEquals( transaction.getId(), nextNodeId );

      assertEquals( observableValue.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

      observableValue.reportObserved();

      //Not tracking so no state updated
      assertEquals( observableValue.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    }, Flags.READ_ONLY, new Object[]{ param1, param2, param3 } );

    assertFalse( context.isTransactionActive() );

    //ObservableValue still not updated
    assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    assertEquals( observableValue.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), null );
      assertEquals( e.returnsResult(), false );
      assertEquals( e.getResult(), null );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void action_procedure()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertInvariantFailure( context::getTransaction,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final ObservableValue<?> observableValue = context.observable();
    assertEquals( observableValue.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final ObservableValue<Object> observableValue1 = Arez.context().observable();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    context.action( name, () -> {
      observableValue1.reportObserved();
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), name );
      assertEquals( transaction.getPrevious(), null );
      assertEquals( transaction.getContext(), context );
      assertEquals( transaction.getId(), nextNodeId );

      assertEquals( observableValue.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

      observableValue.reportObserved();

      //Not tracking so no state updated
      assertEquals( observableValue.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    }, Flags.READ_ONLY, new Object[]{ param1, param2, param3 } );

    assertFalse( context.isTransactionActive() );

    //ObservableValue still not updated
    assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    assertEquals( observableValue.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );

    handler.assertNextEvent( ActionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), null );
      assertEquals( e.returnsResult(), false );
      assertEquals( e.getResult(), null );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void action_procedure_throwsException()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertInvariantFailure( context::getTransaction,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final String name = ValueUtil.randomString();
    final IOException ioException = new IOException();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final ObservableValue observableValue = Arez.context().observable();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final Procedure procedure = () -> {
      observableValue.reportObserved();
      throw ioException;
    };
    assertThrows( IOException.class,
                  () -> context.action( name, procedure, Flags.READ_ONLY, new Object[]{ param1, param2, param3 } ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), false );
      assertEquals( e.getTracker(), null );
    } );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), ioException );
      assertEquals( e.returnsResult(), false );
      assertEquals( e.getResult(), null );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void nestedProceduresAccessingSameObservable()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertThrows( context::getTransaction );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();
    final String name2 = ValueUtil.randomString();
    context.action( name, () -> {
      observeADependency();
      assertTrue( context.isTransactionActive() );
      final Transaction transaction1 = context.getTransaction();
      assertEquals( transaction1.getName(), name );
      assertEquals( transaction1.getPrevious(), null );
      assertEquals( transaction1.getContext(), context );
      assertEquals( transaction1.getId(), nextNodeId );
      assertEquals( transaction1.isRootTransaction(), true );
      assertEquals( transaction1.getRootTransaction(), transaction1 );

      context.action( name2, () -> {
        observeADependency();
        assertTrue( context.isTransactionActive() );
        final Transaction transaction2 = context.getTransaction();
        assertEquals( transaction2.getName(), name2 );
        assertEquals( transaction2.getPrevious(), transaction1 );
        assertEquals( transaction2.getContext(), context );
        assertEquals( transaction2.getId(), nextNodeId + 1 );
        assertEquals( transaction2.isRootTransaction(), false );
        assertEquals( transaction2.getRootTransaction(), transaction1 );
      }, Flags.REQUIRE_NEW_TRANSACTION );

      final Transaction transaction1b = context.getTransaction();
      assertEquals( transaction1b.getName(), name );
      assertEquals( transaction1b.getPrevious(), null );
      assertEquals( transaction1b.getContext(), context );
      assertEquals( transaction1b.getId(), nextNodeId );
      assertEquals( transaction1b.isRootTransaction(), true );
      assertEquals( transaction1b.getRootTransaction(), transaction1b );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void nextNodeId()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertEquals( context.currentNextTransactionId(), 1 );
    assertEquals( context.nextTransactionId(), 1 );
    assertEquals( context.currentNextTransactionId(), 2 );
  }

  @Test
  public void observerErrorHandler()
    throws Exception
  {
    final ArezContext context = Arez.context();
    // Clear out handler added as part of test infrastructure
    context.getObserverErrorHandlerSupport().getObserverErrorHandlers().clear();

    final ObserverError observerError = ObserverError.REACTION_ERROR;
    final Throwable throwable = new Throwable();
    final Procedure action = new NoopProcedure();
    final Observer observer = context.observer( ValueUtil.randomString(), action, Flags.READ_WRITE );

    final AtomicInteger callCount = new AtomicInteger();

    final ObserverErrorHandler handler = ( o, e, t ) -> {
      callCount.incrementAndGet();
      assertEquals( o, observer );
      assertEquals( e, observerError );
      assertEquals( t, throwable );
    };

    context.addObserverErrorHandler( handler );

    assertEquals( context.getObserverErrorHandlerSupport().getObserverErrorHandlers().size(), 1 );
    assertEquals( context.getObserverErrorHandlerSupport().getObserverErrorHandlers().contains( handler ), true );

    assertEquals( callCount.get(), 0 );

    context.reportObserverError( observer, observerError, throwable );

    assertEquals( callCount.get(), 1 );

    context.removeObserverErrorHandler( handler );

    assertEquals( context.getObserverErrorHandlerSupport().getObserverErrorHandlers().size(), 0 );

    context.reportObserverError( observer, observerError, throwable );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void reportObserverError_when_spyEventHandler_present()
    throws Exception
  {
    final ArezContext context = Arez.context();
    // Clear out handler added as part of test infrastructure
    context.getObserverErrorHandlerSupport().getObserverErrorHandlers().clear();

    final ObserverError observerError = ObserverError.REACTION_ERROR;
    final Throwable throwable = new Throwable();
    final Procedure action = new NoopProcedure();
    final Observer observer = context.observer( action );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    context.reportObserverError( observer, observerError, throwable );

    handler.assertEventCount( 1 );

    handler.assertNextEvent( ObserverErrorEvent.class, event -> {
      assertEquals( event.getObserver().getName(), observer.getName() );
      assertEquals( event.getError(), observerError );
      assertEquals( event.getThrowable(), throwable );
    } );
  }

  @Test
  public void addObserverErrorHandler_whenDisabled()
    throws Exception
  {
    ArezTestUtil.disableObserverErrorHandlers();

    final ObserverErrorHandler handler = ( o, e, t ) -> {
    };

    assertInvariantFailure( () -> Arez.context().addObserverErrorHandler( handler ),
                            "Arez-0182: ArezContext.addObserverErrorHandler() invoked when Arez.areObserverErrorHandlersEnabled() returns false." );
  }

  @Test
  public void removeObserverErrorHandler_whenDisabled()
    throws Exception
  {
    ArezTestUtil.disableObserverErrorHandlers();

    final ArezContext context = Arez.context();

    final ObserverErrorHandler handler = ( o, e, t ) -> {
    };

    assertInvariantFailure( () -> context.removeObserverErrorHandler( handler ),
                            "Arez-0181: ArezContext.removeObserverErrorHandler() invoked when Arez.areObserverErrorHandlersEnabled() returns false." );
  }

  @Test
  public void getSpy_whenSpiesDisabled()
    throws Exception
  {
    ArezTestUtil.disableSpies();

    final ArezContext context = Arez.context();

    assertInvariantFailure( context::getSpy, "Arez-0021: Attempting to get Spy but spies are not enabled." );
  }

  @Test
  public void scheduleReaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    context.scheduleReaction( observer );

    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
    assertEquals( context.getScheduler().getPendingObservers().contains( observer ), true );
  }

  @Test
  public void scheduleReaction_shouldAbortInReadOnlyTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    assertInvariantFailure( () -> {
                              final Procedure executable = () -> context.scheduleReaction( observer );
                              context.action( executable, Flags.READ_ONLY );
                            },
                            "Arez-0013: Observer named '" + observer.getName() + "' attempted to be scheduled " +
                            "during read-only transaction." );
  }

  @Test
  public void scheduleReaction_shouldAbortInReadWriteOwnedTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer derivation = context.computed( () -> "" ).getObserver();

    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    setCurrentTransaction( derivation );

    assertInvariantFailure( () -> context.scheduleReaction( derivation ),
                            "Arez-0014: Observer named '" + derivation.getName() + "' attempted to schedule itself " +
                            "during read-only tracking transaction. Observers that are supporting ComputedValue " +
                            "instances must not schedule self." );
  }

  @Test
  public void scheduleReaction_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );
    context.scheduleReaction( observer );

    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );

    handler.assertEventCount( 1 );
    handler.assertNextEvent( ReactionScheduledEvent.class,
                             event -> assertEquals( event.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void computedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final Procedure onActivate = ValueUtil::randomString;
    final Procedure onDeactivate = ValueUtil::randomString;
    final Procedure onStale = ValueUtil::randomString;
    final ComputedValue<String> computedValue =
      context.computed( null,
                        name,
                        function,
                        onActivate,
                        onDeactivate,
                        onStale,
                        Flags.PRIORITY_HIGH );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.getObserver().isKeepAlive(), false );
    assertEquals( computedValue.getObserver().arezOnlyDependencies(), true );
    assertEquals( computedValue.getObservableValue().getName(), name );
    assertEquals( computedValue.getOnActivate(), onActivate );
    assertEquals( computedValue.getOnDeactivate(), onDeactivate );
    assertEquals( computedValue.getOnStale(), onStale );
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObserver().getPriority(), Priority.HIGH );
    assertEquals( computedValue.getObserver().canObserveLowerPriorityDependencies(), false );
  }

  @Test
  public void computedValue_withComponent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final String name = ValueUtil.randomString();
    final ComputedValue<String> computedValue =
      context.computed( component, name, () -> "", null, null, null );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getComponent(), component );
  }

  @Test
  public void computedValue_canObserveLowerPriorityDependencies()
    throws Exception
  {
    final ComputedValue<String> computedValue =
      Arez.context().computed( () -> "", Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    assertEquals( computedValue.getObserver().canObserveLowerPriorityDependencies(), true );
  }

  @Test
  public void computedValue_mayNotAccessArezState()
    throws Exception
  {
    final ComputedValue<String> computedValue =
      Arez.context().computed( () -> "", Flags.NON_AREZ_DEPENDENCIES );
    assertEquals( computedValue.getObserver().arezOnlyDependencies(), false );
  }

  @Test
  public void computedValue_withKeepAliveAndRunImmediately()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> action = () -> {
      observeADependency();
      calls.incrementAndGet();
      return "";
    };
    final ComputedValue<String> computedValue =
      context.computed( action, Flags.KEEPALIVE | Flags.RUN_NOW );

    assertEquals( computedValue.getObserver().isKeepAlive(), true );
    assertEquals( calls.get(), 1 );
  }

  @Test
  public void computedValue_withKeepAliveAndNoRunImmediately()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> action = () -> {
      observeADependency();
      calls.incrementAndGet();
      return "";
    };
    final ComputedValue<String> computedValue = context.computed( action, Flags.KEEPALIVE | Flags.RUN_LATER );

    assertEquals( computedValue.getObserver().isKeepAlive(), true );
    assertEquals( calls.get(), 0 );

    context.triggerScheduler();

    assertEquals( calls.get(), 1 );
  }

  @Test
  public void computedValue_pass_no_hooks()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue = context.computed( name, function );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObservableValue().getName(), name );
    assertEquals( computedValue.getOnActivate(), null );
    assertEquals( computedValue.getOnDeactivate(), null );
    assertEquals( computedValue.getOnStale(), null );
    assertEquals( computedValue.getObserver().getPriority(), Priority.NORMAL );
  }

  @Test
  public void computedValue_minimumParameters()
    throws Exception
  {
    final ArezContext context = Arez.context();

    context.setNextNodeId( 22 );
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue = context.computed( function );

    final String name = "ComputedValue@22";
    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObservableValue().getName(), name );
    assertEquals( computedValue.getOnActivate(), null );
    assertEquals( computedValue.getOnDeactivate(), null );
    assertEquals( computedValue.getOnStale(), null );
    assertEquals( computedValue.getObserver().getPriority(), Priority.NORMAL );
    assertEquals( computedValue.getObserver().canObserveLowerPriorityDependencies(), false );
  }

  @Test
  public void computedValue_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final ComputedValue<String> computedValue =
      context.computed( ValueUtil.randomString(), () -> {
        observeADependency();
        return "";
      } );

    handler.assertEventCount( 1 );

    handler.assertNextEvent( ComputedValueCreatedEvent.class,
                             event -> assertEquals( event.getComputedValue().getName(), computedValue.getName() ) );
  }

  @Test
  public void observer_noObservers()
    throws Exception
  {
    setIgnoreObserverErrors( true );

    Arez.context().setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = Arez.context().observer( callCount::incrementAndGet );

    assertEquals( observer.getName(), "Observer@22" );
    assertEquals( observer.isMutation(), false );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( callCount.get(), 1 );

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( getObserverErrors().get( 0 ),
                  "Observer: Observer@22 Error: REACTION_ERROR java.lang.IllegalStateException: Arez-0172: Observer named 'Observer@22' that does not use an external executor completed observed function but is not observing any properties. As a result the observer will never be rescheduled." );
  }

  @Test
  public void autorun_noObservers_manualReportStaleAllowed()
    throws Exception
  {
    setIgnoreObserverErrors( true );

    final ArezContext context = Arez.context();
    final AtomicInteger callCount = new AtomicInteger();
    context.observer( callCount::incrementAndGet, Flags.NON_AREZ_DEPENDENCIES );

    assertEquals( callCount.get(), 1 );

    // No observer errors even though the executable accesses no arez dependencies
    assertEquals( getObserverErrors().size(), 0 );
  }

  @Test
  public void observer_minimumParameters()
    throws Exception
  {
    final ArezContext context = Arez.context();

    context.setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Procedure observed = () -> {
      observeADependency();
      callCount.incrementAndGet();
    };
    final Observer observer = context.observer( observed );

    assertEquals( observer.getComponent(), null );
    assertEquals( observer.getName(), "Observer@22" );
    assertEquals( observer.isMutation(), false );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.isComputedValue(), false );
    assertEquals( observer.canObserveLowerPriorityDependencies(), false );
    assertEquals( observer.isKeepAlive(), true );
    assertEquals( observer.nestedActionsAllowed(), false );
    assertEquals( observer.getOnDepsChanged(), null );
    assertEquals( observer.isApplicationExecutor(), false );
    assertEquals( observer.getObserved(), observed );
    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void autorun_withComponent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final String name = ValueUtil.randomString();
    final Observer observer = context.observer( component, name, AbstractArezTest::observeADependency );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getComponent(), component );
  }

  @Test
  public void autorun_minimumParametersForMutation()
    throws Exception
  {
    final ArezContext context = Arez.context();

    context.setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.observer( () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, Flags.READ_WRITE );

    assertEquals( observer.getName(), "Observer@22" );
    assertEquals( observer.isMutation(), true );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.nestedActionsAllowed(), false );
    assertEquals( observer.supportsManualSchedule(), false );
    assertEquals( callCount.get(), 1 );
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void autorun_runImmediately()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observableValue = Arez.context().observable();
    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.observer( name, () -> {
      observableValue.reportObserved();
      callCount.incrementAndGet();
    }, Flags.READ_WRITE );

    assertEquals( observer.getName(), name );
    assertEquals( observer.isMutation(), true );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.isApplicationExecutor(), false );
    assertEquals( callCount.get(), 1 );

    handler.assertEventCount( 8 );

    handler.assertNextEvent( ObserverCreatedEvent.class, e -> assertEquals( e.getObserver().getName(), name ) );
    handler.assertNextEvent( ReactionScheduledEvent.class, e -> assertEquals( e.getObserver().getName(), name ) );
    assertObserverReaction( handler, name );
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void autorun_runImmediately_will_obeyNormalSchedulingPriorities()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observableValue = Arez.context().observable();

    final Observer observer1 = context.observer( "O1", observableValue::reportObserved );
    final Observer observer2 = context.observer( "O2", observableValue::reportObserved, Flags.PRIORITY_HIGH );

    final Disposable schedulerLock = context.pauseScheduler();

    // Trigger change that should schedule above observers
    context.safeAction( observableValue::reportChanged );

    final Observer observer3 = context.observer( "O3", observableValue::reportObserved );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    schedulerLock.dispose();

    handler.assertEventCount( 6 * 3 );

    assertObserverReaction( handler, observer2.getName() );
    assertObserverReaction( handler, observer1.getName() );
    assertObserverReaction( handler, observer3.getName() );
  }

  private void assertObserverReaction( @Nonnull final TestSpyEventHandler handler, @Nonnull final String name )
  {
    handler.assertNextEvent( ReactionStartedEvent.class, e -> assertEquals( e.getObserver().getName(), name ) );
    handler.assertNextEvent( ActionStartedEvent.class, e -> assertEquals( e.getName(), name ) );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> assertEquals( e.getName(), name ) );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> assertEquals( e.getName(), name ) );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> assertEquals( e.getName(), name ) );
    handler.assertNextEvent( ReactionCompletedEvent.class, e -> assertEquals( e.getObserver().getName(), name ) );
  }

  @Test
  public void autorun_highPriority()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( AbstractArezTest::observeADependency, Flags.PRIORITY_HIGH );
    assertEquals( observer.getPriority(), Priority.HIGH );
  }

  @Test
  public void autorun_canObserveLowerPriorityDependencies()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Observer observer =
      context.observer( AbstractArezTest::observeADependency, Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    assertEquals( observer.canObserveLowerPriorityDependencies(), true );
  }

  @Test
  public void autorun_nestedActionsAllowed()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Observer observer =
      context.observer( AbstractArezTest::observeADependency, Flags.NESTED_ACTIONS_ALLOWED );

    assertEquals( observer.nestedActionsAllowed(), true );
  }

  @Test
  public void autorun_arezOnlyDependencies()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Observer observer =
      context.observer( AbstractArezTest::observeADependency, Flags.NON_AREZ_DEPENDENCIES );
    assertEquals( observer.arezOnlyDependencies(), false );
  }

  @Test
  public void autorun_supportsManualSchedule()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( AbstractArezTest::observeADependency, ValueUtil::randomString );
    assertEquals( observer.supportsManualSchedule(), true );
  }

  @Test
  public void autorun_notRunImmediately()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.observer( name, () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, Flags.RUN_LATER );

    assertEquals( observer.getName(), name );
    assertEquals( observer.isMutation(), false );
    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.isApplicationExecutor(), false );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( ObserverCreatedEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ReactionScheduledEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void tracker()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.tracker( null,
                                               name,
                                               callCount::incrementAndGet,
                                               Flags.PRIORITY_HIGH |
                                               Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES |
                                               Flags.NESTED_ACTIONS_ALLOWED |
                                               Flags.NON_AREZ_DEPENDENCIES );

    assertEquals( observer.getName(), name );
    assertEquals( observer.isMutation(), false );
    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertEquals( observer.getComponent(), null );
    assertEquals( observer.getPriority(), Priority.HIGH );
    assertEquals( observer.canObserveLowerPriorityDependencies(), true );
    assertEquals( observer.isApplicationExecutor(), true );
    assertEquals( observer.nestedActionsAllowed(), true );
    assertEquals( observer.arezOnlyDependencies(), false );
    assertEquals( observer.supportsManualSchedule(), false );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    handler.assertEventCount( 1 );

    handler.assertNextEvent( ObserverCreatedEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void tracker_withComponent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );
    final Observer observer = context.tracker( component, name, callCount::incrementAndGet );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getComponent(), component );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.canObserveLowerPriorityDependencies(), false );
    assertEquals( observer.isApplicationExecutor(), true );
  }

  @Test
  public void tracker_minimalParameters()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final int nextNodeId = context.getNextNodeId();

    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.tracker( callCount::incrementAndGet );

    assertEquals( observer.getName(), "Observer@" + nextNodeId );
    assertEquals( observer.isMutation(), false );
    assertEquals( observer.getState(), Flags.STATE_INACTIVE );
    assertEquals( observer.canObserveLowerPriorityDependencies(), false );
    assertEquals( observer.isApplicationExecutor(), true );
    assertEquals( observer.nestedActionsAllowed(), false );
    assertEquals( observer.arezOnlyDependencies(), true );
    assertEquals( observer.supportsManualSchedule(), false );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    handler.assertEventCount( 1 );

    handler.assertNextEvent( ObserverCreatedEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void observer_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    context.pauseScheduler();

    final Observer observer = context.observer( new CountingProcedure() );

    handler.assertEventCount( 2 );

    handler.assertNextEvent( ObserverCreatedEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ReactionScheduledEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void createObservable_no_parameters()
    throws Exception
  {
    final ArezContext context = Arez.context();

    context.setNextNodeId( 22 );
    final ObservableValue<?> observableValue = context.observable();

    assertNotNull( observableValue.getName() );
    assertEquals( observableValue.getName(), "ObservableValue@22" );
    assertNull( observableValue.getAccessor() );
    assertNull( observableValue.getMutator() );
  }

  @Test
  public void createObservable()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final ObservableValue<?> observableValue = context.observable( name );

    assertEquals( observableValue.getName(), name );
    assertEquals( observableValue.getAccessor(), null );
    assertEquals( observableValue.getMutator(), null );
  }

  @Test
  public void createObservable_withIntrospectors()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final PropertyAccessor<String> accessor = () -> "";
    final PropertyMutator<String> mutator = v -> {
    };
    final ObservableValue<?> observableValue = context.observable( name, accessor, mutator );

    assertEquals( observableValue.getName(), name );
    assertEquals( observableValue.getAccessor(), accessor );
    assertEquals( observableValue.getMutator(), mutator );
  }

  @Test
  public void createObservable_withComponent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final ObservableValue<String> observableValue = context.observable( component, name );

    assertEquals( observableValue.getName(), name );
    assertEquals( observableValue.getComponent(), component );
  }

  @Test
  public void createObservable_spyEventHandlerPresent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final ObservableValue<?> observableValue = context.observable( name );

    assertEquals( observableValue.getName(), name );
    handler.assertEventCount( 1 );
    handler.assertNextEvent( ObservableValueCreatedEvent.class,
                             e -> assertEquals( e.getObservableValue().getName(), observableValue.getName() ) );
  }

  @Test
  public void createObservable_name_Null()
    throws Exception
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable( null );

    assertNotNull( observableValue );
  }

  @Test
  public void pauseScheduler()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertEquals( context.isSchedulerPaused(), false );

    assertEquals( context.getSchedulerLockCount(), 0 );
    final Disposable lock1 = context.pauseScheduler();
    assertEquals( context.getSchedulerLockCount(), 1 );
    assertEquals( context.isSchedulerPaused(), true );

    final AtomicInteger callCount = new AtomicInteger();

    // This would normally be scheduled and run now but scheduler should be paused
    context.observer( () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, Flags.RUN_LATER );
    context.triggerScheduler();

    assertEquals( callCount.get(), 0 );

    final Disposable lock2 = context.pauseScheduler();
    assertEquals( context.getSchedulerLockCount(), 2 );
    assertEquals( context.isSchedulerPaused(), true );

    lock2.dispose();

    assertEquals( context.getSchedulerLockCount(), 1 );

    // Already disposed so this is a noop
    lock2.dispose();

    assertEquals( context.getSchedulerLockCount(), 1 );
    assertEquals( context.isSchedulerPaused(), true );

    assertEquals( callCount.get(), 0 );

    lock1.dispose();

    assertEquals( context.getSchedulerLockCount(), 0 );
    assertEquals( callCount.get(), 1 );
    assertEquals( context.isSchedulerPaused(), false );
  }

  @Test
  public void releaseSchedulerLock_whenNoLock()
  {
    assertInvariantFailure( () -> Arez.context().releaseSchedulerLock(),
                            "Arez-0016: releaseSchedulerLock() reduced schedulerLockCount below 0." );
  }

  @Test
  public void createComponent()
  {
    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();
    final String name = ValueUtil.randomString();

    assertFalse( context.isComponentPresent( type, id ) );

    final Component component = context.component( type, id, name );

    assertTrue( context.isComponentPresent( type, id ) );

    assertEquals( component.getType(), type );
    assertEquals( component.getId(), id );
    assertEquals( component.getName(), name );
    assertEquals( component.getPreDispose(), null );
    assertEquals( component.getPostDispose(), null );
  }

  @Test
  public void createComponent_includeDisposeHooks()
  {
    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();
    final String name = ValueUtil.randomString();

    assertFalse( context.isComponentPresent( type, id ) );

    final SafeProcedure preDispose = () -> {
    };
    final SafeProcedure postDispose = () -> {
    };
    final Component component = context.component( type, id, name, preDispose, postDispose );

    assertTrue( context.isComponentPresent( type, id ) );

    assertEquals( component.getType(), type );
    assertEquals( component.getId(), id );
    assertEquals( component.getName(), name );
    assertEquals( component.getPreDispose(), preDispose );
    assertEquals( component.getPostDispose(), postDispose );
  }

  @Test
  public void createComponent_synthesizeNameIfRequired()
  {
    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();

    assertFalse( context.isComponentPresent( type, id ) );

    final Component component = context.component( type, id );

    assertTrue( context.isComponentPresent( type, id ) );

    assertEquals( component.getType(), type );
    assertEquals( component.getId(), id );
    assertEquals( component.getName(), type + "@" + id );
  }

  @Test
  public void createComponent_spyEventHandlerPresent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    handler.assertEventCount( 1 );
    handler.assertNextEvent( ComponentCreateStartedEvent.class,
                             event -> assertEquals( event.getComponentInfo().getName(), component.getName() ) );
  }

  @Test
  public void createComponent_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> context.component( type, id, name ),
                            "Arez-0008: ArezContext.component() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void createComponent_duplicateComponent()
  {
    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();

    context.component( type, id, ValueUtil.randomString() );

    assertTrue( context.isComponentPresent( type, id ) );

    assertInvariantFailure( () -> context.component( type, id, ValueUtil.randomString() ),
                            "Arez-0009: ArezContext.component() invoked for type '" + type + "' and id '" + id +
                            "' but a component already exists for specified type+id." );
  }

  @Test
  public void isComponentPresent_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();

    assertInvariantFailure( () -> context.isComponentPresent( type, id ),
                            "Arez-0135: ArezContext.isComponentPresent() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void deregisterComponent_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final Component component =
      new Component( context,
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    assertInvariantFailure( () -> context.deregisterComponent( component ),
                            "Arez-0006: ArezContext.deregisterComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void deregisterComponent_componentMisalignment()
  {
    final ArezContext context = Arez.context();

    final Component component =
      new Component( context,
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final Component component2 =
      context.component( component.getType(), component.getId(), ValueUtil.randomString() );

    assertInvariantFailure( () -> context.deregisterComponent( component ),
                            "Arez-0007: ArezContext.deregisterComponent() invoked for '" +
                            component + "' but was unable to remove specified component from registry. " +
                            "Actual component removed: " + component2 );
  }

  @Test
  public void deregisterComponent_removesTypeIfLastOfType()
  {
    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final Component component =
      context.component( type, ValueUtil.randomString(), ValueUtil.randomString() );
    final Component component2 =
      context.component( type, ValueUtil.randomString(), ValueUtil.randomString() );

    assertEquals( context.findAllComponentTypes().size(), 1 );
    assertEquals( context.findAllComponentTypes().contains( type ), true );

    context.deregisterComponent( component );

    assertEquals( context.findAllComponentTypes().size(), 1 );
    assertEquals( context.findAllComponentTypes().contains( type ), true );

    context.deregisterComponent( component2 );

    assertEquals( context.findAllComponentTypes().size(), 0 );
  }

  @Test
  public void component_finders()
  {
    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id1 = ValueUtil.randomString();
    final String id2 = ValueUtil.randomString();

    assertEquals( context.findAllComponentTypes().size(), 0 );
    assertEquals( context.findAllComponentsByType( type ).size(), 0 );

    final Component component = context.component( type, id1, ValueUtil.randomString() );

    assertEquals( context.findAllComponentTypes().size(), 1 );
    assertEquals( context.findAllComponentTypes().contains( type ), true );

    assertEquals( context.findAllComponentsByType( ValueUtil.randomString() ).size(), 0 );

    assertEquals( context.findAllComponentsByType( type ).size(), 1 );
    assertEquals( context.findAllComponentsByType( type ).contains( component ), true );

    final Component component2 = context.component( type, id2, ValueUtil.randomString() );

    assertEquals( context.findAllComponentTypes().size(), 1 );
    assertEquals( context.findAllComponentTypes().contains( type ), true );

    assertEquals( context.findAllComponentsByType( ValueUtil.randomString() ).size(), 0 );

    assertEquals( context.findAllComponentsByType( type ).size(), 2 );
    assertEquals( context.findAllComponentsByType( type ).contains( component ), true );
    assertEquals( context.findAllComponentsByType( type ).contains( component2 ), true );

    assertEquals( context.findComponent( type, id1 ), component );
    assertEquals( context.findComponent( type, id2 ), component2 );
    assertEquals( context.findComponent( type, ValueUtil.randomString() ), null );
    assertEquals( context.findComponent( ValueUtil.randomString(), id2 ), null );
  }

  @Test
  public void findComponent_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();

    assertInvariantFailure( () -> context.findComponent( type, id ),
                            "Arez-0010: ArezContext.findComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllComponentsByType_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();

    assertInvariantFailure( () -> context.findAllComponentsByType( type ),
                            "Arez-0011: ArezContext.findAllComponentsByType() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllComponentTypes_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    assertInvariantFailure( context::findAllComponentTypes,
                            "Arez-0012: ArezContext.findAllComponentTypes() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void registryAccessWhenDisabled()
  {
    ArezTestUtil.disableRegistries();

    final ArezContext context = Arez.context();

    final ObservableValue<Object> observableValue = context.observable();
    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = context.observer( AbstractArezTest::observeADependency );

    assertInvariantFailure( () -> context.registerObservableValue( observableValue ),
                            "Arez-0022: ArezContext.registerObservableValue invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( () -> context.deregisterObservableValue( observableValue ),
                            "Arez-0024: ArezContext.deregisterObservableValue invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( context::getTopLevelObservables,
                            "Arez-0026: ArezContext.getTopLevelObservables() invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( () -> context.registerObserver( observer ),
                            "Arez-0027: ArezContext.registerObserver invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( () -> context.deregisterObserver( observer ),
                            "Arez-0029: ArezContext.deregisterObserver invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( context::getTopLevelObservers,
                            "Arez-0031: ArezContext.getTopLevelObservers() invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( () -> context.registerComputedValue( computedValue ),
                            "Arez-0032: ArezContext.registerComputedValue invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( () -> context.deregisterComputedValue( computedValue ),
                            "Arez-0034: ArezContext.deregisterComputedValue invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( context::getTopLevelComputedValues,
                            "Arez-0036: ArezContext.getTopLevelComputedValues() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void observableRegistry()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observableValue = context.observable();

    assertEquals( context.getTopLevelObservables().size(), 1 );
    assertEquals( context.getTopLevelObservables().get( observableValue.getName() ), observableValue );

    assertInvariantFailure( () -> context.registerObservableValue( observableValue ),
                            "Arez-0023: ArezContext.registerObservableValue invoked with observableValue named '" +
                            observableValue.getName() + "' but an existing observableValue with that name is " +
                            "already registered." );

    assertEquals( context.getTopLevelObservables().size(), 1 );
    context.getTopLevelObservables().clear();
    assertEquals( context.getTopLevelObservables().size(), 0 );

    assertInvariantFailure( () -> context.deregisterObservableValue( observableValue ),
                            "Arez-0025: ArezContext.deregisterObservableValue invoked with observableValue named '" +
                            observableValue.getName() + "' but no observableValue with that name is registered." );
  }

  @Test
  public void observerRegistry()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( AbstractArezTest::observeADependency );

    assertEquals( context.getTopLevelObservers().size(), 1 );
    assertEquals( context.getTopLevelObservers().get( observer.getName() ), observer );

    assertInvariantFailure( () -> context.registerObserver( observer ),
                            "Arez-0028: ArezContext.registerObserver invoked with observer named '" +
                            observer.getName() + "' but an existing observer with that name is " +
                            "already registered." );

    assertEquals( context.getTopLevelObservers().size(), 1 );
    context.getTopLevelObservers().clear();
    assertEquals( context.getTopLevelObservers().size(), 0 );

    assertInvariantFailure( () -> context.deregisterObserver( observer ),
                            "Arez-0030: ArezContext.deregisterObserver invoked with observer named '" +
                            observer.getName() + "' but no observer with that name is registered." );
  }

  @Test
  public void computedValueRegistry()
  {
    final ArezContext context = Arez.context();

    final ComputedValue computedValue = context.computed( () -> "" );

    assertEquals( context.getTopLevelComputedValues().size(), 1 );
    assertEquals( context.getTopLevelComputedValues().get( computedValue.getName() ), computedValue );

    assertInvariantFailure( () -> context.registerComputedValue( computedValue ),
                            "Arez-0033: ArezContext.registerComputedValue invoked with computed value " +
                            "named '" +
                            computedValue.getName() +
                            "' but an existing computed value with that name " +
                            "is already registered." );

    assertEquals( context.getTopLevelComputedValues().size(), 1 );
    context.getTopLevelComputedValues().clear();
    assertEquals( context.getTopLevelComputedValues().size(), 0 );

    assertInvariantFailure( () -> context.deregisterComputedValue( computedValue ),
                            "Arez-0035: ArezContext.deregisterComputedValue invoked with computed value named '" +
                            computedValue.getName() + "' but no computed value with that name is registered." );
  }

  @Test
  public void scheduleDispose()
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = Arez.context().observer( new CountAndObserveProcedure() );

    assertEquals( scheduler.getPendingObservers().size(), 0 );

    context.scheduleDispose( observer );

    assertEquals( scheduler.getPendingDisposes().size(), 1 );
    assertEquals( scheduler.getPendingDisposes().contains( observer ), true );
  }

  @Test
  public void locator()
  {
    final ArezContext context = Arez.context();

    final Locator locator = context.locator();
    assertNotNull( locator );

    assertNull( locator.findById( String.class, "21" ) );

    final TypeBasedLocator worker = new TypeBasedLocator();
    worker.registerLookup( String.class, String::valueOf );

    final Disposable disposable = context.registerLocator( worker );

    assertEquals( locator.findById( String.class, "21" ), "21" );

    disposable.dispose();

    assertNull( locator.findById( String.class, "21" ) );
  }

  @Test
  public void locator_referencesDisabled()
  {
    ArezTestUtil.disableReferences();
    ArezTestUtil.resetState();

    assertInvariantFailure( () -> Arez.context().locator(),
                            "Arez-0192: ArezContext.locator() invoked but Arez.areReferencesEnabled() returned false." );
  }

  @Test
  public void registerLocator_referencesDisabled()
  {
    ArezTestUtil.disableReferences();
    ArezTestUtil.resetState();

    assertInvariantFailure( () -> Arez.context().registerLocator( new TypeBasedLocator() ),
                            "Arez-0191: ArezContext.registerLocator invoked but Arez.areReferencesEnabled() returned false." );
  }
}
