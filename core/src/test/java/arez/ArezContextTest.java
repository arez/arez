package arez;

import arez.component.TypeBasedLocator;
import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ComponentCreateStartEvent;
import arez.spy.ComponentInfo;
import arez.spy.ComputableValueCreateEvent;
import arez.spy.ComputeCompleteEvent;
import arez.spy.ComputeStartEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.ObservableValueCreateEvent;
import arez.spy.ObserveCompleteEvent;
import arez.spy.ObserveScheduleEvent;
import arez.spy.ObserveStartEvent;
import arez.spy.ObserverCreateEvent;
import arez.spy.ObserverErrorEvent;
import arez.spy.ObserverInfo;
import arez.spy.Priority;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
import arez.spy.TaskCompleteEvent;
import arez.spy.TaskStartEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "ConstantConditions" )
public final class ArezContextTest
  extends AbstractTest
{
  @Test
  public void zoneEnabledConstruction()
  {
    ArezTestUtil.enableZones();

    final ArezContext context = Arez.context();

    final Zone defaultZone = Arez.currentZone();

    assertEquals( context.getZone(), defaultZone );

    final Zone zone = Arez.createZone();
    zone.safeRun( () -> {
      assertNotEquals( Arez.context().getZone(), defaultZone );
      assertEquals( Arez.context().getZone(), zone );
    } );
  }

  @Test
  public void generateName()
  {
    final ArezContext context = Arez.context();

    // Use passed in name
    assertEquals( context.generateName( "ComputableValue", "MyName" ), "MyName" );

    //synthesize name
    context.setNextNodeId( 1 );
    assertEquals( context.generateName( "ComputableValue", null ), "ComputableValue@1" );
    assertEquals( context.getNextNodeId(), 2 );

    ArezTestUtil.disableNames();

    //Ignore name
    assertNull( context.generateName( "ComputableValue", "MyName" ) );

    //Null name also fine
    assertNull( context.generateName( "ComputableValue", null ) );
  }

  @Test
  public void triggerScheduler()
  {
    final ArezContext context = Arez.context();
    final AtomicInteger callCount = new AtomicInteger();

    context.observer( () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, Observer.Flags.RUN_LATER );

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
    }, Observer.Flags.RUN_LATER );

    assertEquals( callCount.get(), 0 );

    context.markSchedulerAsActive();

    context.triggerScheduler();

    assertEquals( callCount.get(), 0 );
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
      }, ActionFlags.READ_ONLY );
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
      }, ActionFlags.READ_ONLY );
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
      observeADependency();
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

  @Test
  public void isComputableTransactionActive()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isReadOnlyTransactionActive() );
    assertFalse( context.isReadWriteTransactionActive() );
    assertFalse( context.isTrackingTransactionActive() );
    assertFalse( context.isComputableTransactionActive() );

    context.action( () -> {
      assertTrue( context.isTransactionActive() );
      assertFalse( context.isReadOnlyTransactionActive() );
      assertTrue( context.isReadWriteTransactionActive() );
      assertFalse( context.isComputableTransactionActive() );
      observeADependency();
    } );

    context.computable( () -> {
      observeADependency();
      assertTrue( context.isTransactionActive() );
      assertTrue( context.isReadOnlyTransactionActive() );
      assertFalse( context.isReadWriteTransactionActive() );
      assertTrue( context.isTrackingTransactionActive() );
      assertTrue( context.isComputableTransactionActive() );
      return 0;
    } );

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isReadOnlyTransactionActive() );
    assertFalse( context.isReadWriteTransactionActive() );
    assertFalse( context.isTrackingTransactionActive() );
    assertFalse( context.isComputableTransactionActive() );
  }

  @Test
  public void registerOnDeactivationHook_basic()
  {
    final ArezContext context = Arez.context();

    final CountingProcedure onDeactivateHook1 = new CountingProcedure();
    final CountingProcedure onDeactivateHook2 = new CountingProcedure();
    final CountingProcedure onDeactivateHook3 = new CountingProcedure();

    final ComputableValue<Integer> computable =
      context.computable( () -> {
        observeADependency();
        context.registerOnDeactivateHook( onDeactivateHook1 );
        context.registerOnDeactivateHook( onDeactivateHook2 );
        context.registerOnDeactivateHook( onDeactivateHook3 );
        return 0;
      } );

    // Computed observed by observer with all deactivate hooks active when deactivated

    assertEquals( computable.getObserver().getOnDeactivateHooks().size(), 0 );

    final Observer observer = context.observer( () -> assertEquals( computable.get(), (Integer) 0 ) );

    assertEquals( computable.getObserver().getOnDeactivateHooks().size(), 3 );

    assertEquals( onDeactivateHook1.getCallCount(), 0 );
    assertEquals( onDeactivateHook2.getCallCount(), 0 );
    assertEquals( onDeactivateHook3.getCallCount(), 0 );

    Disposable.dispose( observer );

    assertEquals( computable.getObserver().getOnDeactivateHooks().size(), 0 );

    assertEquals( onDeactivateHook1.getCallCount(), 1 );
    assertEquals( onDeactivateHook2.getCallCount(), 1 );
    assertEquals( onDeactivateHook3.getCallCount(), 1 );
  }

  @Test
  public void registerOnDeactivationHook_dynamicallyAdjustHooks()
  {
    final ArezContext context = Arez.context();

    final AtomicBoolean includeAll = new AtomicBoolean( true );

    final CountingProcedure onDeactivateHook1 = new CountingProcedure();
    final CountingProcedure onDeactivateHook2 = new CountingProcedure();
    final CountingProcedure onDeactivateHook3 = new CountingProcedure();

    final ObservableValue<Object> observable = Arez.context().observable();

    final ComputableValue<Integer> computable =
      context.computable( () -> {
        observable.reportObserved();

        context.registerOnDeactivateHook( onDeactivateHook1 );
        if ( includeAll.get() )
        {
          context.registerOnDeactivateHook( onDeactivateHook2 );
          context.registerOnDeactivateHook( onDeactivateHook3 );
        }
        return 0;
      } );

    assertEquals( computable.getObserver().getOnDeactivateHooks().size(), 0 );

    final Observer observer = context.observer( () -> assertEquals( computable.get(), (Integer) 0 ) );

    assertEquals( computable.getObserver().getOnDeactivateHooks().size(), 3 );

    assertEquals( onDeactivateHook1.getCallCount(), 0 );
    assertEquals( onDeactivateHook2.getCallCount(), 0 );
    assertEquals( onDeactivateHook3.getCallCount(), 0 );

    // Retrigger computed so it reduces the number of OnDeactivateHooks
    includeAll.set( false );
    context.safeAction( observable::reportChanged, ActionFlags.READ_WRITE );

    assertEquals( computable.getObserver().getOnDeactivateHooks().size(), 1 );

    assertEquals( onDeactivateHook1.getCallCount(), 0 );
    assertEquals( onDeactivateHook2.getCallCount(), 0 );
    assertEquals( onDeactivateHook3.getCallCount(), 0 );

    Disposable.dispose( observer );

    assertEquals( computable.getObserver().getOnDeactivateHooks().size(), 0 );

    assertEquals( onDeactivateHook1.getCallCount(), 1 );
    assertEquals( onDeactivateHook2.getCallCount(), 0 );
    assertEquals( onDeactivateHook3.getCallCount(), 0 );
  }

  @Test
  public void registerOnDeactivationHook_nullHook()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<Integer> computable =
      context.computable( () -> {
        observeADependency();
        assertInvariantFailure( () -> context.registerOnDeactivateHook( null ),
                                "Arez-0124: registerOnDeactivateHook() invoked with a null callback." );
        return 0;
      } );

    assertEquals( computable.getObserver().getOnDeactivateHooks().size(), 0 );

    context.observer( () -> assertEquals( computable.get(), (Integer) 0 ) );

    assertEquals( computable.getObserver().getOnDeactivateHooks().size(), 0 );
  }

  @Test
  public void registerOnDeactivationHook_outsideTransaction()
  {
    final ArezContext context = Arez.context();

    assertInvariantFailure( () -> context.registerOnDeactivateHook( new NoopProcedure() ),
                            "Arez-0098: registerOnDeactivateHook() invoked outside of a transaction." );
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
                      ActionFlags.NO_VERIFY_ACTION_REQUIRED | ActionFlags.REQUIRE_NEW_TRANSACTION );

      final int result1 = context.action( () -> {
        assertNotEquals( context.getTransaction(), transaction );
        return 0;
      }, ActionFlags.NO_VERIFY_ACTION_REQUIRED | ActionFlags.REQUIRE_NEW_TRANSACTION );

      context.safeAction( () -> assertNotEquals( context.getTransaction(), transaction ),
                          ActionFlags.NO_VERIFY_ACTION_REQUIRED | ActionFlags.REQUIRE_NEW_TRANSACTION );

      final int result2 =
        context.safeAction( () -> {
          assertNotEquals( context.getTransaction(), transaction );
          return 0;
        }, ActionFlags.NO_VERIFY_ACTION_REQUIRED | ActionFlags.REQUIRE_NEW_TRANSACTION );

      context.action( () -> assertEquals( context.getTransaction(), transaction ),
                      ActionFlags.NO_VERIFY_ACTION_REQUIRED );

      final int result3 = context.action( () -> {
        assertEquals( context.getTransaction(), transaction );
        return 0;
      }, ActionFlags.NO_VERIFY_ACTION_REQUIRED );

      context.safeAction( () -> assertEquals( context.getTransaction(), transaction ),
                          ActionFlags.NO_VERIFY_ACTION_REQUIRED );

      final int result4 =
        context.safeAction( () -> {
          assertEquals( context.getTransaction(), transaction );
          return 0;
        }, ActionFlags.NO_VERIFY_ACTION_REQUIRED );
    }, ActionFlags.NO_VERIFY_ACTION_REQUIRED );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void nestedAction_allowed()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger updateCalled = new AtomicInteger();

    final Observer tracker =
      context.tracker( updateCalled::incrementAndGet,
                       Observer.Flags.READ_WRITE | Observer.Flags.NESTED_ACTIONS_ALLOWED );

    context.observe( tracker, () -> {
      observeADependency();
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();

      context.action( () -> assertNotEquals( context.getTransaction(), transaction ),
                      ActionFlags.NO_VERIFY_ACTION_REQUIRED | ActionFlags.REQUIRE_NEW_TRANSACTION );

      final int result1 = context.action( () -> {
        assertNotEquals( context.getTransaction(), transaction );
        return 0;
      }, ActionFlags.NO_VERIFY_ACTION_REQUIRED | ActionFlags.REQUIRE_NEW_TRANSACTION );

      context.safeAction( () -> assertNotEquals( context.getTransaction(), transaction ),
                          ActionFlags.NO_VERIFY_ACTION_REQUIRED | ActionFlags.REQUIRE_NEW_TRANSACTION );

      final int result2 = context.safeAction( () -> {
        assertNotEquals( context.getTransaction(), transaction );
        return 0;
      }, ActionFlags.NO_VERIFY_ACTION_REQUIRED | ActionFlags.REQUIRE_NEW_TRANSACTION );

      context.action( () -> assertEquals( context.getTransaction(), transaction ),
                      ActionFlags.NO_VERIFY_ACTION_REQUIRED );
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
      context.tracker( updateCalled::incrementAndGet,
                       Observer.Flags.READ_WRITE | Observer.Flags.NESTED_ACTIONS_DISALLOWED );

    context.observe( tracker, () -> {
      observeADependency();
      assertInvariantFailure( () -> context.action( "A1",
                                                    AbstractTest::observeADependency,
                                                    ActionFlags.NO_VERIFY_ACTION_REQUIRED |
                                                    ActionFlags.REQUIRE_NEW_TRANSACTION ),
                              "Arez-0187: Attempting to nest action named 'A1' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );

      assertInvariantFailure( () -> context.action( "A2",
                                                    () -> 1,
                                                    ActionFlags.NO_VERIFY_ACTION_REQUIRED |
                                                    ActionFlags.REQUIRE_NEW_TRANSACTION ),
                              "Arez-0187: Attempting to nest action named 'A2' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );

      assertInvariantFailure( () -> context.safeAction( "A3",
                                                        AbstractTest::observeADependency,
                                                        ActionFlags.NO_VERIFY_ACTION_REQUIRED |
                                                        ActionFlags.REQUIRE_NEW_TRANSACTION ),
                              "Arez-0187: Attempting to nest action named 'A3' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );

      assertInvariantFailure( () -> context.safeAction( "A4",
                                                        () -> 1,
                                                        ActionFlags.NO_VERIFY_ACTION_REQUIRED |
                                                        ActionFlags.REQUIRE_NEW_TRANSACTION ),
                              "Arez-0187: Attempting to nest action named 'A4' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );
    } );
  }

  @SuppressWarnings( "MagicConstant" )
  @Test
  public void verifyActionFlags()
  {
    final Procedure executable = () -> {
    };
    assertInvariantFailure( () -> Arez.context().action( executable, Observer.Flags.DEACTIVATE_ON_UNOBSERVE ),
                            "Arez-0212: Flags passed to action 'Action@1' include some " +
                            "unexpected flags set: " + Observer.Flags.DEACTIVATE_ON_UNOBSERVE );
  }

  @Test
  public void verifyActionFlags_badTransactionFlags()
  {
    final Procedure executable = () -> {
    };
    assertInvariantFailure( () -> Arez.context()
                              .action( executable, ActionFlags.READ_ONLY | ActionFlags.READ_WRITE ),
                            "Arez-0126: Flags passed to action 'Action@1' include both READ_ONLY and READ_WRITE." );
  }

  @Test
  public void verifyActionFlags_badVerifyAction()
  {
    final Procedure executable = () -> {
    };
    assertInvariantFailure( () -> Arez.context()
                              .action( executable, ActionFlags.VERIFY_ACTION_REQUIRED | ActionFlags.NO_VERIFY_ACTION_REQUIRED ),
                            "Arez-0127: Flags passed to action 'Action@1' include both VERIFY_ACTION_REQUIRED and NO_VERIFY_ACTION_REQUIRED." );
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

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final String v0 =
      context.action( name, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), name );
        assertNull( transaction.getPrevious() );
        assertEquals( transaction.getContext(), context );
        assertEquals( transaction.getId(), nextNodeId );
        assertFalse( transaction.isMutation() );

        assertEquals( observableValue.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

        observableValue.reportObserved();

        //Not tracking so no state updated
        assertEquals( observableValue.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

        return expectedValue;
      }, ActionFlags.READ_ONLY, new Object[]{ param1, param2, param3 } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    //ObservableValue still not updated
    assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    assertEquals( observableValue.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertNull( e.getThrowable() );
      assertTrue( e.returnsResult() );
      assertEquals( e.getResult(), v0 );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void action_NO_REPORT_RESULT()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    context.action( () -> {
      observableValue.reportObserved();

      return ValueUtil.randomString();
    }, ActionFlags.NO_REPORT_RESULT );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> assertNull( e.getResult() ) );
  }

  @Test
  public void safeAction_NO_REPORT_RESULT()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    context.safeAction( () -> {
      observableValue.reportObserved();

      return ValueUtil.randomString();
    }, ActionFlags.NO_REPORT_RESULT );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> assertNull( e.getResult() ) );
  }

  @Test
  public void action_function_throwsException()
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

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    assertThrows( IOException.class, () ->
      context.action( name, () -> {
        throw ioException;
      }, 0, new Object[]{ param1, param2, param3 } ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertTrue( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertTrue( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), ioException );
      assertTrue( e.returnsResult() );
      assertNull( e.getResult() );
      assertFalse( e.isTracked() );
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
      assertTrue( context.getTransaction().isMutation() );
      assertEquals( context.getTransaction().getName(), name );
      return ValueUtil.randomString();
    } );
  }

  @Test
  public void action_procedure_verifyActionRequired_false()
    throws Throwable
  {
    final Procedure executable = ValueUtil::randomString;
    Arez.context().action( executable, ActionFlags.NO_VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_procedure_verifyActionRequired_true_butInvariantsDisabled()
    throws Throwable
  {
    ArezTestUtil.noCheckInvariants();

    final Procedure executable = ValueUtil::randomString;
    Arez.context().action( executable, ActionFlags.VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_procedure_verifyActionRequired_true()
  {
    final Procedure procedure = ValueUtil::randomString;
    assertInvariantFailure( () -> Arez.context().action( "X", procedure, ActionFlags.VERIFY_ACTION_REQUIRED ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void action_procedure_verifyActionRequired_true_is_default()
  {
    assertInvariantFailure( () -> Arez.context().action( "X", (Procedure) ValueUtil::randomString ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void action_function_verifyActionRequired_false()
    throws Throwable
  {
    Arez.context().action( (Function<String>) ValueUtil::randomString, ActionFlags.NO_VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_function_verifyActionRequired_true_butInvariantsDisabled()
    throws Throwable
  {
    ArezTestUtil.noCheckInvariants();

    Arez.context().action( (Function<String>) ValueUtil::randomString, ActionFlags.VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_function_verifyActionRequired_true()
  {
    final Function<String> function = ValueUtil::randomString;
    assertInvariantFailure( () -> Arez.context().action( "X", function, ActionFlags.VERIFY_ACTION_REQUIRED ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void action_function_verifyActionRequired_true_is_default()
  {
    final Function<String> function = ValueUtil::randomString;
    assertInvariantFailure( () -> Arez.context().action( "X", function ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_false()
  {
    final SafeProcedure procedure = ValueUtil::randomString;
    Arez.context().safeAction( ValueUtil.randomString(), procedure, ActionFlags.NO_VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_true_butInvariantsDisabled()
  {
    ArezTestUtil.noCheckInvariants();

    final SafeProcedure executable = ValueUtil::randomString;
    Arez.context().safeAction( ValueUtil.randomString(), executable, ActionFlags.VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_true()
  {
    final SafeProcedure procedure = ValueUtil::randomString;
    assertInvariantFailure( () -> Arez.context().safeAction( "X", procedure, ActionFlags.VERIFY_ACTION_REQUIRED ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_true_is_default()
  {
    assertInvariantFailure( () -> Arez.context().safeAction( "X", (SafeProcedure) ValueUtil::randomString ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void safeAction_function_verifyActionRequired_false()
  {
    Arez.context().safeAction( (SafeFunction<String>) ValueUtil::randomString, ActionFlags.NO_VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_function_verifyActionRequired_true_butInvariantsDisabled()
  {
    ArezTestUtil.noCheckInvariants();

    Arez.context().safeAction( (SafeFunction<String>) ValueUtil::randomString, ActionFlags.VERIFY_ACTION_REQUIRED );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_function_verifyActionRequired_true()
  {
    final SafeFunction<String> function = ValueUtil::randomString;
    assertInvariantFailure( () -> Arez.context().safeAction( "X", function, ActionFlags.VERIFY_ACTION_REQUIRED ),
                            "Arez-0185: Action named 'X' completed but no reads, writes, schedules, reportStales or reportPossiblyChanged occurred within the scope of the action." );
  }

  @Test
  public void safeAction_function_verifyActionRequired_true_is_default()
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

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final String v0 =
      context.action( () -> {
        observableValue.reportObserved();
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), "Action@" + nextNodeId );
        assertTrue( transaction.isMutation() );

        return expectedValue;
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 0 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertTrue( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertTrue( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertNull( e.getThrowable() );
      assertTrue( e.returnsResult() );
      assertEquals( e.getResult(), v0 );
      assertFalse( e.isTracked() );
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

    final Observer tracker = context.tracker( callCount::incrementAndGet, Observer.Flags.READ_WRITE );

    final ObservableValue<?> observableValue = context.observable();
    assertEquals( observableValue.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

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
        final List<ObservableValue<?>> observableValues = transaction.getObservableValues();
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

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertTrue( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertTrue( e.isMutation() );
      final ObserverInfo info = e.getTracker();
      assertNotNull( info );
      assertEquals( info.getName(), tracker.getName() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertTrue( e.isMutation() );
      final ObserverInfo info = e.getTracker();
      assertNotNull( info );
      assertEquals( info.getName(), tracker.getName() );
    } );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertNull( e.getThrowable() );
      assertTrue( e.returnsResult() );
      assertEquals( e.getResult(), v0 );
      assertTrue( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void observe_function_no_parameters()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    // Scheduler paused otherwise reactions will run in environment and upset our environment call count
    context.pauseScheduler();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer =
      context.tracker( callCount::incrementAndGet, Observer.Flags.AREZ_OR_NO_DEPENDENCIES | Observer.Flags.READ_WRITE );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final int result =
      context.observe( observer, () -> {
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), observer.getName() );
        return 23;
      } );

    assertEquals( result, 23 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertTrue( e.isTracked() );
      assertEquals( e.getParameters().length, 0 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertTrue( e.isMutation() );
      final ObserverInfo info = e.getTracker();
      assertNotNull( info );
      assertEquals( info.getName(), observer.getName() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertTrue( e.isMutation() );
      final ObserverInfo info = e.getTracker();
      assertNotNull( info );
      assertEquals( info.getName(), observer.getName() );
    } );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertNull( e.getThrowable() );
      assertTrue( e.returnsResult() );
      assertEquals( e.getResult(), result );
      assertTrue( e.isTracked() );
      assertEquals( e.getParameters().length, 0 );
    } );
  }

  @Test
  public void observe_NO_REPORT_RESULT()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer =
      context.tracker( callCount::incrementAndGet,
                       Observer.Flags.AREZ_OR_NO_DEPENDENCIES | Observer.Flags.NO_REPORT_RESULT );

    assertTrue( observer.noReportResults() );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final int result = context.observe( observer, () -> 23 );

    assertEquals( result, 23 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertTrue( e.returnsResult() );
      assertNull( e.getResult() );
    } );
  }

  @Test
  public void track_function_passingNonTracker()
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

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final String v0 =
      context.safeAction( name, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), name );
        assertNull( transaction.getPrevious() );
        assertEquals( transaction.getContext(), context );
        assertEquals( transaction.getId(), nextNodeId );
        assertFalse( transaction.isMutation() );

        assertEquals( observableValue.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

        observableValue.reportObserved();

        //Not tracking so no state updated
        assertEquals( observableValue.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

        return expectedValue;
      }, ActionFlags.READ_ONLY, new Object[]{ param1, param2, param3 } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    //ObservableValue still not updated
    assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    assertEquals( observableValue.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertNull( e.getThrowable() );
      assertTrue( e.returnsResult() );
      assertEquals( e.getResult(), v0 );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void action_safeFunction_throws_Exception()
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertInvariantFailure( context::getTransaction,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final IllegalStateException secException = new IllegalStateException( "" );

    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    assertThrows( IllegalStateException.class, () ->
      context.safeAction( name, () -> {
        throw secException;
      }, ActionFlags.READ_ONLY, new Object[]{ param1, param2, param3 } ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), secException );
      assertTrue( e.returnsResult() );
      assertNull( e.getResult() );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void action_safeFunction_minimalParameters()
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
        assertTrue( transaction.isMutation() );
        return expectedValue;
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void action_safeFunction_NameButNoMutationVariant()
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    context.safeAction( name, () -> {
      observeADependency();
      assertTrue( context.getTransaction().isMutation() );
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
        final List<ObservableValue<?>> observableValues = transaction.getObservableValues();
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
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final int nextNodeId = context.getNextNodeId();
    context.safeAction( () -> {
      observeADependency();
      assertTrue( context.isTransactionActive() );
      assertTrue( context.getTransaction().isMutation() );
      assertEquals( context.getTransaction().getName(), "Action@" + nextNodeId );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void safeAction_safeProcedure_NameButNoMutationVariant()
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    context.safeAction( name, () -> {
      observeADependency();
      assertTrue( context.getTransaction().isMutation() );
      assertEquals( context.getTransaction().getName(), name );
    } );
  }

  @Test
  public void action_safeProcedure_throws_Exception()
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertInvariantFailure( context::getTransaction,
                            "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final IllegalStateException secException = new IllegalStateException( "" );

    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final SafeProcedure procedure = () -> {
      throw secException;
    };
    assertThrows( IllegalStateException.class,
                  () -> context.safeAction( name, procedure, 0, new Object[]{ param1, param2, param3 } ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertTrue( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertTrue( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), secException );
      assertFalse( e.returnsResult() );
      assertNull( e.getResult() );
      assertFalse( e.isTracked() );
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
      final List<ObservableValue<?>> observableValues = transaction.getObservableValues();
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
      assertTrue( context.getTransaction().isMutation() );
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
      assertTrue( context.getTransaction().isMutation() );
      assertEquals( context.getTransaction().getName(), "Action@" + nextNodeId );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void track_procedure_passingNonTracker()
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
      final List<ObservableValue<?>> observableValues = transaction.getObservableValues();
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

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    context.safeAction( name, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), name );
      assertNull( transaction.getPrevious() );
      assertEquals( transaction.getContext(), context );
      assertEquals( transaction.getId(), nextNodeId );

      assertEquals( observableValue.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

      observableValue.reportObserved();

      //Not tracking so no state updated
      assertEquals( observableValue.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    }, ActionFlags.READ_ONLY, new Object[]{ param1, param2, param3 } );

    assertFalse( context.isTransactionActive() );

    //ObservableValue still not updated
    assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    assertEquals( observableValue.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertNull( e.getThrowable() );
      assertFalse( e.returnsResult() );
      assertNull( e.getResult() );
      assertFalse( e.isTracked() );
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

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    context.action( name, () -> {
      observableValue1.reportObserved();
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), name );
      assertNull( transaction.getPrevious() );
      assertEquals( transaction.getContext(), context );
      assertEquals( transaction.getId(), nextNodeId );

      assertEquals( observableValue.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );

      observableValue.reportObserved();

      //Not tracking so no state updated
      assertEquals( observableValue.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    }, ActionFlags.READ_ONLY, new Object[]{ param1, param2, param3 } );

    assertFalse( context.isTransactionActive() );

    //ObservableValue still not updated
    assertNotEquals( nextNodeId, observableValue.getLastTrackerTransactionId() );
    assertEquals( observableValue.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );

    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertNull( e.getThrowable() );
      assertFalse( e.returnsResult() );
      assertNull( e.getResult() );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
  }

  @Test
  public void action_procedure_throwsException()
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

    final ObservableValue<?> observableValue = Arez.context().observable();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final Procedure procedure = () -> {
      observableValue.reportObserved();
      throw ioException;
    };
    assertThrows( IOException.class,
                  () -> context.action( name,
                                        procedure,
                                        ActionFlags.READ_ONLY,
                                        new Object[]{ param1, param2, param3 } ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    handler.assertNextEvent( ActionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isTracked() );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    } );
    handler.assertNextEvent( TransactionStartEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertFalse( e.isMutation() );
      assertNull( e.getTracker() );
    } );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertEquals( e.getThrowable(), ioException );
      assertFalse( e.returnsResult() );
      assertNull( e.getResult() );
      assertFalse( e.isTracked() );
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
      assertNull( transaction1.getPrevious() );
      assertEquals( transaction1.getContext(), context );
      assertEquals( transaction1.getId(), nextNodeId );
      assertTrue( transaction1.isRootTransaction() );
      assertEquals( transaction1.getRootTransaction(), transaction1 );

      context.action( name2, () -> {
        observeADependency();
        assertTrue( context.isTransactionActive() );
        final Transaction transaction2 = context.getTransaction();
        assertEquals( transaction2.getName(), name2 );
        assertEquals( transaction2.getPrevious(), transaction1 );
        assertEquals( transaction2.getContext(), context );
        assertEquals( transaction2.getId(), nextNodeId + 1 );
        assertFalse( transaction2.isRootTransaction() );
        assertEquals( transaction2.getRootTransaction(), transaction1 );
      }, ActionFlags.REQUIRE_NEW_TRANSACTION );

      final Transaction transaction1b = context.getTransaction();
      assertEquals( transaction1b.getName(), name );
      assertNull( transaction1b.getPrevious() );
      assertEquals( transaction1b.getContext(), context );
      assertEquals( transaction1b.getId(), nextNodeId );
      assertTrue( transaction1b.isRootTransaction() );
      assertEquals( transaction1b.getRootTransaction(), transaction1b );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void nextNodeId()
  {
    final ArezContext context = Arez.context();

    assertEquals( context.currentNextTransactionId(), 1 );
    assertEquals( context.nextTransactionId(), 1 );
    assertEquals( context.currentNextTransactionId(), 2 );
  }

  @Test
  public void observer_with_onDepsUpdated()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observable = context.observable();
    final AtomicInteger observedCallCount = new AtomicInteger();
    final AtomicInteger onDepsChangeCallCount = new AtomicInteger();

    final String name = ValueUtil.randomString();
    context.observer( name, () -> {
      observedCallCount.incrementAndGet();
      observable.reportObserved();
      assertEquals( context.getTransaction().getName(), name );
    }, onDepsChangeCallCount::incrementAndGet );

    assertEquals( onDepsChangeCallCount.get(), 0 );

    context.safeAction( observable::reportChanged );

    assertEquals( onDepsChangeCallCount.get(), 1 );
  }

  @Test
  public void observer_withComponent_and_onDepsUpdated()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observable = context.observable();
    final AtomicInteger observeCallCount = new AtomicInteger();
    final AtomicInteger onDepsChangeCallCount = new AtomicInteger();

    final Component component = context.component( ValueUtil.randomString(), 22 );

    final String name = ValueUtil.randomString();
    final Observer observer =
      context.observer( component, name, () -> {
        observeCallCount.incrementAndGet();
        observable.reportObserved();
        assertEquals( context.getTransaction().getName(), name );
      }, onDepsChangeCallCount::incrementAndGet );

    assertEquals( onDepsChangeCallCount.get(), 0 );
    final ComponentInfo componentInfo = observer.asInfo().getComponent();
    assertNotNull( componentInfo );
    assertEquals( componentInfo.getName(), component.getName() );

    context.safeAction( observable::reportChanged );

    assertEquals( onDepsChangeCallCount.get(), 1 );
  }

  @Test
  public void observerErrorHandler()
  {
    final ArezContext context = Arez.context();
    // Clear out handler added as part of test infrastructure
    context.getObserverErrorHandlerSupport().getHandlers().clear();

    final ObserverError observerError = ObserverError.REACTION_ERROR;
    final Throwable throwable = new Throwable();
    final Procedure action = new NoopProcedure();
    final Observer observer = context.observer( ValueUtil.randomString(), action, Observer.Flags.READ_WRITE );

    final AtomicInteger callCount = new AtomicInteger();

    final ObserverErrorHandler handler = ( o, e, t ) -> {
      callCount.incrementAndGet();
      assertEquals( o, observer );
      assertEquals( e, observerError );
      assertEquals( t, throwable );
    };

    context.addObserverErrorHandler( handler );

    assertEquals( context.getObserverErrorHandlerSupport().getHandlers().size(), 1 );
    assertTrue( context.getObserverErrorHandlerSupport().getHandlers().contains( handler ) );

    assertEquals( callCount.get(), 0 );

    context.reportObserverError( observer, observerError, throwable );

    assertEquals( callCount.get(), 1 );

    context.removeObserverErrorHandler( handler );

    assertEquals( context.getObserverErrorHandlerSupport().getHandlers().size(), 0 );

    context.reportObserverError( observer, observerError, throwable );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void reportObserverError_when_spyEventHandler_present()
  {
    final ArezContext context = Arez.context();
    // Clear out handler added as part of test infrastructure
    context.getObserverErrorHandlerSupport().getHandlers().clear();

    final ObserverError observerError = ObserverError.REACTION_ERROR;
    final Throwable throwable = new Throwable();
    final Procedure action = new NoopProcedure();
    final Observer observer = context.observer( action );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

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
  {
    ArezTestUtil.disableObserverErrorHandlers();

    final ObserverErrorHandler handler = ( o, e, t ) -> {
    };

    assertInvariantFailure( () -> Arez.context().addObserverErrorHandler( handler ),
                            "Arez-0182: ArezContext.addObserverErrorHandler() invoked when Arez.areObserverErrorHandlersEnabled() returns false." );
  }

  @Test
  public void removeObserverErrorHandler_whenDisabled()
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
  {
    ArezTestUtil.disableSpies();

    final ArezContext context = Arez.context();

    assertInvariantFailure( context::getSpy, "Arez-0021: Attempting to get Spy but spies are not enabled." );
  }

  @Test
  public void scheduleReaction()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 0L );

    context.scheduleReaction( observer );

    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 1L );
    assertTrue( context.getTaskQueue().getOrderedTasks().anyMatch( o -> o == observer.getTask() ) );
  }

  @Test
  public void scheduleReaction_shouldAbortInReadOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 0L );

    assertInvariantFailure( () -> {
                              final Procedure executable = () -> context.scheduleReaction( observer );
                              context.action( executable, ActionFlags.READ_ONLY );
                            },
                            "Arez-0013: Observer named '" + observer.getName() + "' attempted to be scheduled " +
                            "during read-only transaction." );
  }

  @Test
  public void scheduleReaction_shouldAbortInReadWriteOwnedTransaction()
  {
    final ArezContext context = Arez.context();

    final Observer derivation = context.computable( () -> "" ).getObserver();

    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 0L );

    setCurrentTransaction( derivation );

    assertInvariantFailure( () -> context.scheduleReaction( derivation ),
                            "Arez-0014: Observer named '" + derivation.getName() + "' attempted to schedule itself " +
                            "during read-only tracking transaction. Observers that are supporting ComputableValue " +
                            "instances must not schedule self." );
  }

  @Test
  public void scheduleReaction_generates_spyEvent()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 0L );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );
    context.scheduleReaction( observer );

    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 1L );

    handler.assertEventCount( 1 );
    handler.assertNextEvent( ObserveScheduleEvent.class,
                             event -> assertEquals( event.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void computableValue()
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final Procedure onActivate = ValueUtil::randomString;
    final ComputableValue<String> computableValue =
      context.computable( null,
                          name,
                          function,
                          onActivate,
                          ComputableValue.Flags.PRIORITY_HIGH );

    assertEquals( computableValue.getName(), name );
    assertEquals( computableValue.getContext(), context );
    assertFalse( computableValue.getObserver().isKeepAlive() );
    assertTrue( computableValue.getObserver().areArezDependenciesRequired() );
    assertEquals( computableValue.getObservableValue().getName(), name );
    assertEquals( computableValue.getOnActivate(), onActivate );
    assertEquals( computableValue.getObserver().getName(), name );
    assertEquals( computableValue.getObserver().getTask().getPriority(), Priority.HIGH );
    assertFalse( computableValue.getObserver().canObserveLowerPriorityDependencies() );
  }

  @Test
  public void computable_with_NO_REPORT_RESULT()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observable = Arez.context().observable();
    final SafeFunction<String> function = () -> {
      observable.reportObserved();
      return "";
    };
    final ComputableValue<String> computableValue =
      context.computable( function, ComputableValue.Flags.NO_REPORT_RESULT );

    assertTrue( computableValue.getObserver().noReportResults() );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    context.safeAction( computableValue::get );

    handler.assertEventCount( 9 );

    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );

    handler.assertNextEvent( ComputeStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );

    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class, e -> assertNull( e.getResult() ) );

    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );
  }

  @Test
  public void computableValue_withComponent()
  {
    final ArezContext context = Arez.context();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final String name = ValueUtil.randomString();
    final ComputableValue<String> computableValue =
      context.computable( component, name, () -> "", null );

    assertEquals( computableValue.getName(), name );
    assertEquals( computableValue.getComponent(), component );
  }

  @Test
  public void computableValue_canObserveLowerPriorityDependencies()
  {
    final ComputableValue<String> computableValue =
      Arez.context().computable( () -> "", ComputableValue.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    assertTrue( computableValue.getObserver().canObserveLowerPriorityDependencies() );
  }

  @Test
  public void computableValue_mayNotAccessArezState()
  {
    final ArezContext context = Arez.context();
    assertFalse( context.computable( () -> "", ComputableValue.Flags.AREZ_OR_NO_DEPENDENCIES )
                   .getObserver()
                   .areArezDependenciesRequired() );
    assertFalse( context.computable( () -> "", ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES )
                   .getObserver()
                   .areArezDependenciesRequired() );
  }

  @Test
  public void computableValue_withKeepAliveAndRunImmediately()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> action = () -> {
      observeADependency();
      calls.incrementAndGet();
      return "";
    };
    final ComputableValue<String> computableValue =
      context.computable( action, ComputableValue.Flags.KEEPALIVE | ComputableValue.Flags.RUN_NOW );

    assertTrue( computableValue.getObserver().isKeepAlive() );
    assertEquals( calls.get(), 1 );
  }

  @Test
  public void computableValue_withKeepAliveAndNoRunImmediately()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> action = () -> {
      observeADependency();
      calls.incrementAndGet();
      return "";
    };
    final ComputableValue<String> computableValue =
      context.computable( action, ComputableValue.Flags.KEEPALIVE | ComputableValue.Flags.RUN_LATER );

    assertTrue( computableValue.getObserver().isKeepAlive() );
    assertEquals( calls.get(), 0 );

    context.triggerScheduler();

    assertEquals( calls.get(), 1 );
  }

  @Test
  public void computableValue_pass_no_hooks()
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( name, function );

    assertEquals( computableValue.getName(), name );
    assertEquals( computableValue.getContext(), context );
    assertEquals( computableValue.getObserver().getName(), name );
    assertEquals( computableValue.getObservableValue().getName(), name );
    assertNull( computableValue.getOnActivate() );
    assertEquals( computableValue.getObserver().getTask().getPriority(), Priority.NORMAL );
  }

  @Test
  public void computableValue_minimumParameters()
  {
    final ArezContext context = Arez.context();

    context.setNextNodeId( 22 );
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function );

    final String name = "ComputableValue@22";
    assertEquals( computableValue.getName(), name );
    assertEquals( computableValue.getContext(), context );
    assertEquals( computableValue.getObserver().getName(), name );
    assertEquals( computableValue.getObservableValue().getName(), name );
    assertNull( computableValue.getOnActivate() );
    assertEquals( computableValue.getObserver().getTask().getPriority(), Priority.NORMAL );
    assertFalse( computableValue.getObserver().canObserveLowerPriorityDependencies() );
  }

  @Test
  public void computableValue_generates_spyEvent()
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final ComputableValue<String> computableValue =
      context.computable( ValueUtil.randomString(), () -> {
        observeADependency();
        return "";
      } );

    handler.assertEventCount( 1 );

    handler.assertNextEvent( ComputableValueCreateEvent.class,
                             event -> assertEquals( event.getComputableValue().getName(), computableValue.getName() ) );
  }

  @Test
  public void observer_noObservers()
  {
    ignoreObserverErrors();

    Arez.context().setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = Arez.context().observer( callCount::incrementAndGet );

    assertEquals( observer.getName(), "Observer@22" );
    assertFalse( observer.isMutation() );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getTask().getPriority(), Priority.NORMAL );
    assertEquals( callCount.get(), 1 );

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( getObserverErrors().get( 0 ),
                  "Observer: Observer@22 Error: REACTION_ERROR java.lang.IllegalStateException: Arez-0172: Observer named 'Observer@22' that does not use an external executor completed observe function but is not observing any properties. As a result the observer will never be rescheduled." );
  }

  @Test
  public void autorun_noObservers_manualReportStaleAllowed()
  {
    ignoreObserverErrors();

    final ArezContext context = Arez.context();
    final AtomicInteger callCount = new AtomicInteger();
    context.observer( callCount::incrementAndGet, Observer.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( callCount.get(), 1 );

    // No observer errors even though the executable accesses no arez dependencies
    assertEquals( getObserverErrors().size(), 0 );
  }

  @Test
  public void observer_minimumParameters()
  {
    final ArezContext context = Arez.context();

    context.setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Procedure observe = () -> {
      observeADependency();
      callCount.incrementAndGet();
    };
    final Observer observer = context.observer( observe );

    assertNull( observer.getComponent() );
    assertEquals( observer.getName(), "Observer@22" );
    assertFalse( observer.isMutation() );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getTask().getPriority(), Priority.NORMAL );
    assertFalse( observer.isComputableValue() );
    assertFalse( observer.canObserveLowerPriorityDependencies() );
    assertTrue( observer.isKeepAlive() );
    assertFalse( observer.nestedActionsAllowed() );
    assertNull( observer.getOnDepsChange() );
    assertFalse( observer.isApplicationExecutor() );
    assertEquals( observer.getObserve(), observe );
    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void autorun_withComponent()
  {
    final ArezContext context = Arez.context();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final String name = ValueUtil.randomString();
    final Observer observer = context.observer( component, name, AbstractTest::observeADependency );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getComponent(), component );
  }

  @Test
  public void autorun_minimumParametersForMutation()
  {
    final ArezContext context = Arez.context();

    context.setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.observer( () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, Observer.Flags.READ_WRITE );

    assertEquals( observer.getName(), "Observer@22" );
    assertTrue( observer.isMutation() );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getTask().getPriority(), Priority.NORMAL );
    assertFalse( observer.nestedActionsAllowed() );
    assertFalse( observer.supportsManualSchedule() );
    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void autorun_runImmediately()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observableValue = Arez.context().observable();
    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.observer( name, () -> {
      observableValue.reportObserved();
      callCount.incrementAndGet();
    }, Observer.Flags.READ_WRITE );

    assertEquals( observer.getName(), name );
    assertTrue( observer.isMutation() );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( observer.getTask().getPriority(), Priority.NORMAL );
    assertFalse( observer.isApplicationExecutor() );
    assertEquals( callCount.get(), 1 );

    handler.assertEventCount( 8 );

    handler.assertNextEvent( ObserverCreateEvent.class, e -> assertEquals( e.getObserver().getName(), name ) );
    handler.assertNextEvent( ObserveScheduleEvent.class, e -> assertEquals( e.getObserver().getName(), name ) );
    assertObserverReaction( handler, name );
  }

  @Test
  public void autorun_runImmediately_will_obeyNormalSchedulingPriorities()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observableValue = Arez.context().observable();

    final Observer observer1 = context.observer( "O1", observableValue::reportObserved );
    final Observer observer2 = context.observer( "O2", observableValue::reportObserved, Observer.Flags.PRIORITY_HIGH );

    final SchedulerLock schedulerLock = context.pauseScheduler();

    // Trigger change that should schedule above observers
    context.safeAction( observableValue::reportChanged );

    final Observer observer3 = context.observer( "O3", observableValue::reportObserved );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    schedulerLock.dispose();

    handler.assertEventCount( 6 * 3 );

    assertObserverReaction( handler, observer2.getName() );
    assertObserverReaction( handler, observer1.getName() );
    assertObserverReaction( handler, observer3.getName() );
  }

  private void assertObserverReaction( @Nonnull final TestSpyEventHandler handler, @Nonnull final String name )
  {
    handler.assertNextEvent( ObserveStartEvent.class, e -> assertEquals( e.getObserver().getName(), name ) );
    handler.assertNextEvent( ActionStartEvent.class, e -> assertEquals( e.getName(), name ) );
    handler.assertNextEvent( TransactionStartEvent.class, e -> assertEquals( e.getName(), name ) );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> assertEquals( e.getName(), name ) );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> assertEquals( e.getName(), name ) );
    handler.assertNextEvent( ObserveCompleteEvent.class, e -> assertEquals( e.getObserver().getName(), name ) );
  }

  @Test
  public void autorun_highPriority()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( AbstractTest::observeADependency, Observer.Flags.PRIORITY_HIGH );
    assertEquals( observer.getTask().getPriority(), Priority.HIGH );
  }

  @Test
  public void autorun_canObserveLowerPriorityDependencies()
  {
    final ArezContext context = Arez.context();
    final Observer observer =
      context.observer( AbstractTest::observeADependency, Observer.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    assertTrue( observer.canObserveLowerPriorityDependencies() );
  }

  @Test
  public void autorun_nestedActionsAllowed()
  {
    final ArezContext context = Arez.context();
    final Observer observer =
      context.observer( AbstractTest::observeADependency, Observer.Flags.NESTED_ACTIONS_ALLOWED );

    assertTrue( observer.nestedActionsAllowed() );
  }

  @Test
  public void observer_areArezDependenciesRequired()
  {
    final ArezContext context = Arez.context();
    final Procedure observe = AbstractTest::observeADependency;
    assertFalse( context.observer( observe, Observer.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES )
                   .areArezDependenciesRequired() );
    assertFalse( context.observer( observe, Observer.Flags.AREZ_OR_NO_DEPENDENCIES ).areArezDependenciesRequired() );
    assertTrue( context.observer( observe, Observer.Flags.AREZ_DEPENDENCIES ).areArezDependenciesRequired() );
  }

  @Test
  public void autorun_supportsManualSchedule()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( AbstractTest::observeADependency, ValueUtil::randomString );
    assertTrue( observer.supportsManualSchedule() );
  }

  @Test
  public void autorun_notRunImmediately()
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.observer( name, () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, Observer.Flags.RUN_LATER );

    assertEquals( observer.getName(), name );
    assertFalse( observer.isMutation() );
    assertEquals( observer.getState(), Observer.Flags.STATE_INACTIVE );
    assertEquals( observer.getTask().getPriority(), Priority.NORMAL );
    assertFalse( observer.isApplicationExecutor() );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 1L );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( ObserverCreateEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ObserveScheduleEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void tracker()
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.tracker( null,
                                               name,
                                               callCount::incrementAndGet,
                                               Observer.Flags.PRIORITY_HIGH |
                                               Observer.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES |
                                               Observer.Flags.NESTED_ACTIONS_ALLOWED |
                                               Observer.Flags.AREZ_OR_NO_DEPENDENCIES );

    assertEquals( observer.getName(), name );
    assertFalse( observer.isMutation() );
    assertEquals( observer.getState(), Observer.Flags.STATE_INACTIVE );
    assertNull( observer.getComponent() );
    assertEquals( observer.getTask().getPriority(), Priority.HIGH );
    assertTrue( observer.canObserveLowerPriorityDependencies() );
    assertTrue( observer.isApplicationExecutor() );
    assertTrue( observer.nestedActionsAllowed() );
    assertFalse( observer.areArezDependenciesRequired() );
    assertFalse( observer.supportsManualSchedule() );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 0L );

    handler.assertEventCount( 1 );

    handler.assertNextEvent( ObserverCreateEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void tracker_withComponent()
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );
    final Observer observer = context.tracker( component, name, callCount::incrementAndGet );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getComponent(), component );
    assertEquals( observer.getTask().getPriority(), Priority.NORMAL );
    assertFalse( observer.canObserveLowerPriorityDependencies() );
    assertTrue( observer.isApplicationExecutor() );
  }

  @Test
  public void tracker_minimalParameters()
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final int nextNodeId = context.getNextNodeId();

    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.tracker( callCount::incrementAndGet );

    assertEquals( observer.getName(), "Observer@" + nextNodeId );
    assertFalse( observer.isMutation() );
    assertEquals( observer.getState(), Observer.Flags.STATE_INACTIVE );
    assertFalse( observer.canObserveLowerPriorityDependencies() );
    assertTrue( observer.isApplicationExecutor() );
    assertFalse( observer.nestedActionsAllowed() );
    assertTrue( observer.areArezDependenciesRequired() );
    assertFalse( observer.supportsManualSchedule() );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getTaskQueue().getOrderedTasks().count(), 0L );

    handler.assertEventCount( 1 );

    handler.assertNextEvent( ObserverCreateEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void observer_generates_spyEvent()
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    context.pauseScheduler();

    final Observer observer = context.observer( new CountingProcedure() );

    handler.assertEventCount( 2 );

    handler.assertNextEvent( ObserverCreateEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
    handler.assertNextEvent( ObserveScheduleEvent.class,
                             e -> assertEquals( e.getObserver().getName(), observer.getName() ) );
  }

  @Test
  public void createObservable_no_parameters()
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
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final ObservableValue<?> observableValue = context.observable( name );

    assertEquals( observableValue.getName(), name );
    assertNull( observableValue.getAccessor() );
    assertNull( observableValue.getMutator() );
  }

  @Test
  public void createObservable_withIntrospectors()
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
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final String name = ValueUtil.randomString();
    final ObservableValue<?> observableValue = context.observable( name );

    assertEquals( observableValue.getName(), name );
    handler.assertEventCount( 1 );
    handler.assertNextEvent( ObservableValueCreateEvent.class,
                             e -> assertEquals( e.getObservableValue().getName(), observableValue.getName() ) );
  }

  @Test
  public void createObservable_name_Null()
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();

    final ObservableValue<?> observableValue = context.observable( null );

    assertNotNull( observableValue );
  }

  @Test
  public void pauseScheduler()
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isSchedulerPaused() );

    assertEquals( context.getSchedulerLockCount(), 0 );
    final SchedulerLock lock1 = context.pauseScheduler();
    assertEquals( context.getSchedulerLockCount(), 1 );
    assertTrue( context.isSchedulerPaused() );

    final AtomicInteger callCount = new AtomicInteger();

    // This would normally be scheduled and run now but scheduler should be paused
    context.observer( () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, Observer.Flags.RUN_LATER );
    context.triggerScheduler();

    assertEquals( callCount.get(), 0 );

    final SchedulerLock lock2 = context.pauseScheduler();
    assertEquals( context.getSchedulerLockCount(), 2 );
    assertTrue( context.isSchedulerPaused() );

    lock2.dispose();

    assertEquals( context.getSchedulerLockCount(), 1 );

    // Already disposed so this is a noop
    lock2.dispose();

    assertEquals( context.getSchedulerLockCount(), 1 );
    assertTrue( context.isSchedulerPaused() );

    assertEquals( callCount.get(), 0 );

    lock1.dispose();

    assertEquals( context.getSchedulerLockCount(), 0 );
    assertEquals( callCount.get(), 1 );
    assertFalse( context.isSchedulerPaused() );
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
    assertNull( component.getPreDispose() );
    assertNull( component.getPostDispose() );
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
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    handler.assertEventCount( 1 );
    handler.assertNextEvent( ComponentCreateStartEvent.class,
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
    assertTrue( context.findAllComponentTypes().contains( type ) );

    context.deregisterComponent( component );

    assertEquals( context.findAllComponentTypes().size(), 1 );
    assertTrue( context.findAllComponentTypes().contains( type ) );

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
    assertTrue( context.findAllComponentTypes().contains( type ) );

    assertEquals( context.findAllComponentsByType( ValueUtil.randomString() ).size(), 0 );

    assertEquals( context.findAllComponentsByType( type ).size(), 1 );
    assertTrue( context.findAllComponentsByType( type ).contains( component ) );

    final Component component2 = context.component( type, id2, ValueUtil.randomString() );

    assertEquals( context.findAllComponentTypes().size(), 1 );
    assertTrue( context.findAllComponentTypes().contains( type ) );

    assertEquals( context.findAllComponentsByType( ValueUtil.randomString() ).size(), 0 );

    assertEquals( context.findAllComponentsByType( type ).size(), 2 );
    assertTrue( context.findAllComponentsByType( type ).contains( component ) );
    assertTrue( context.findAllComponentsByType( type ).contains( component2 ) );

    assertEquals( context.findComponent( type, id1 ), component );
    assertEquals( context.findComponent( type, id2 ), component2 );
    assertNull( context.findComponent( type, ValueUtil.randomString() ) );
    assertNull( context.findComponent( ValueUtil.randomString(), id2 ) );
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
    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = context.observer( AbstractTest::observeADependency );
    final Task task = context.task( ValueUtil::randomString );

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
    assertInvariantFailure( () -> context.registerComputableValue( computableValue ),
                            "Arez-0032: ArezContext.registerComputableValue invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( () -> context.deregisterComputableValue( computableValue ),
                            "Arez-0034: ArezContext.deregisterComputableValue invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( context::getTopLevelComputableValues,
                            "Arez-0036: ArezContext.getTopLevelComputableValues() invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( () -> context.registerTask( task ),
                            "Arez-0214: ArezContext.registerTask invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( () -> context.deregisterTask( task ),
                            "Arez-0226: ArezContext.deregisterTask invoked when Arez.areRegistriesEnabled() returns false." );
    assertInvariantFailure( context::getTopLevelTasks,
                            "Arez-0228: ArezContext.getTopLevelTasks() invoked when Arez.areRegistriesEnabled() returns false." );
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

    final Observer observer = context.observer( AbstractTest::observeADependency );

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
  public void computableValueRegistry()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<?> computableValue = context.computable( () -> "" );

    assertEquals( context.getTopLevelComputableValues().size(), 1 );
    assertEquals( context.getTopLevelComputableValues().get( computableValue.getName() ), computableValue );

    assertInvariantFailure( () -> context.registerComputableValue( computableValue ),
                            "Arez-0033: ArezContext.registerComputableValue invoked with ComputableValue " +
                            "named '" + computableValue.getName() + "' but an existing ComputableValue with that " +
                            "name is already registered." );

    assertEquals( context.getTopLevelComputableValues().size(), 1 );
    context.getTopLevelComputableValues().clear();
    assertEquals( context.getTopLevelComputableValues().size(), 0 );

    assertInvariantFailure( () -> context.deregisterComputableValue( computableValue ),
                            "Arez-0035: ArezContext.deregisterComputableValue invoked with " +
                            "ComputableValue named '" + computableValue.getName() + "' but no ComputableValue " +
                            "with that name is registered." );
  }

  @Test
  public void taskRegistry()
  {
    final ArezContext context = Arez.context();

    final Task task = context.task( ValueUtil::randomString );

    assertEquals( context.getTopLevelTasks().size(), 1 );
    assertEquals( context.getTopLevelTasks().get( task.getName() ), task );

    assertInvariantFailure( () -> context.registerTask( task ),
                            "Arez-0225: ArezContext.registerTask invoked with Task named '" +
                            task.getName() + "' but an existing Task with that name is already registered." );

    assertEquals( context.getTopLevelTasks().size(), 1 );
    context.getTopLevelTasks().clear();
    assertEquals( context.getTopLevelTasks().size(), 0 );

    assertInvariantFailure( () -> context.deregisterTask( task ),
                            "Arez-0227: ArezContext.deregisterTask invoked with Task named '" +
                            task.getName() + "' but no Task with that name is registered." );
  }

  @Test
  public void computedValueNotPopulateOtherTopLevelRegistries()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<?> computableValue = context.computable( () -> "" );

    assertEquals( context.getTopLevelComputableValues().size(), 1 );
    assertEquals( context.getTopLevelComputableValues().get( computableValue.getName() ), computableValue );

    assertEquals( context.getTopLevelTasks().size(), 0 );
    assertEquals( context.getTopLevelObservers().size(), 0 );
    assertEquals( context.getTopLevelObservables().size(), 0 );
  }

  @Test
  public void observersNotPopulateOtherTopLevelRegistries()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( ValueUtil::randomString, Observer.Flags.AREZ_OR_NO_DEPENDENCIES );

    assertEquals( context.getTopLevelObservers().size(), 1 );
    assertEquals( context.getTopLevelObservers().get( observer.getName() ), observer );

    assertEquals( context.getTopLevelTasks().size(), 0 );
    assertEquals( context.getTopLevelComputableValues().size(), 0 );
    assertEquals( context.getTopLevelObservables().size(), 0 );
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

    assertInvariantFailure( () -> Arez.context().locator(),
                            "Arez-0192: ArezContext.locator() invoked but Arez.areReferencesEnabled() returned false." );
  }

  @Test
  public void registerLocator_referencesDisabled()
  {
    ArezTestUtil.disableReferences();

    assertInvariantFailure( () -> Arez.context().registerLocator( new TypeBasedLocator() ),
                            "Arez-0191: ArezContext.registerLocator invoked but Arez.areReferencesEnabled() returned false." );
  }

  @Test
  public void isSchedulerActive_insideTask()
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isSchedulerActive() );
    context.task( () -> assertTrue( context.isSchedulerActive() ) );
    assertFalse( context.isSchedulerActive() );
  }

  @Test
  public void isSchedulerActive_insideObserver()
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isSchedulerActive() );
    context.observer( () -> assertTrue( context.isSchedulerActive() ), Observer.Flags.AREZ_OR_NO_DEPENDENCIES );
    assertFalse( context.isSchedulerActive() );
  }

  @Test
  public void task()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();
    final String name = ValueUtil.randomString();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final Task task = context.task( name, callCount::incrementAndGet, Task.Flags.PRIORITY_NORMAL );

    assertEquals( task.getName(), name );
    assertEquals( callCount.get(), 1 );
    assertFalse( task.isQueued() );
    assertFalse( task.isDisposed() );

    handler.assertEventCount( 2 );

    handler.assertNextEvent( TaskStartEvent.class, e -> assertEquals( e.getTask().getName(), name ) );
    handler.assertNextEvent( TaskCompleteEvent.class, e -> {
      assertEquals( e.getTask().getName(), name );
      assertNull( e.getThrowable() );
    } );

    handler.reset();

    // This does nothing but just to make sure
    task.dispose();

    assertEquals( callCount.get(), 1 );
    assertFalse( task.isQueued() );
    assertTrue( task.isDisposed() );

    handler.assertEventCount( 0 );
  }

  @Test
  public void task_throwsException()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();
    final String name = ValueUtil.randomString();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final String errorMessage = "Blah Error!";
    final SafeProcedure work = () -> {
      callCount.incrementAndGet();
      throw new RuntimeException( errorMessage );
    };
    final Task task = context.task( name, work, Task.Flags.PRIORITY_NORMAL );

    assertEquals( task.getName(), name );
    assertEquals( callCount.get(), 1 );
    assertFalse( task.isQueued() );
    assertFalse( task.isDisposed() );

    handler.assertEventCount( 2 );

    handler.assertNextEvent( TaskStartEvent.class, e -> assertEquals( e.getTask().getName(), name ) );
    handler.assertNextEvent( TaskCompleteEvent.class, e -> {
      assertEquals( e.getTask().getName(), name );
      assertNotNull( e.getThrowable() );
      assertEquals( e.getThrowable().getMessage(), errorMessage );
    } );
  }

  @Test
  public void task_minimalParameters()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe( context );

    final Task task = context.task( callCount::incrementAndGet );

    final String name = "Task@1";
    assertEquals( task.getName(), name );
    assertEquals( callCount.get(), 1 );
    assertFalse( task.isQueued() );
    assertFalse( task.isDisposed() );

    handler.assertEventCount( 2 );

    handler.assertNextEvent( TaskStartEvent.class, e -> assertEquals( e.getTask().getName(), name ) );
    handler.assertNextEvent( TaskCompleteEvent.class, e -> assertEquals( e.getTask().getName(), name ) );
  }

  @Test
  public void task_RUN_LATER()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final Task task = context.task( null, callCount::incrementAndGet, Task.Flags.RUN_LATER );

    final String name = "Task@1";
    assertEquals( task.getName(), name );
    assertEquals( callCount.get(), 0 );
    assertTrue( task.isQueued() );
    assertFalse( task.isDisposed() );

    handler.assertEventCount( 0 );

    // Trigger scheduler and allow task to run
    context.triggerScheduler();

    assertEquals( callCount.get(), 1 );
    assertFalse( task.isQueued() );
    assertFalse( task.isDisposed() );

    handler.assertEventCount( 2 );

    handler.assertNextEvent( TaskStartEvent.class, e -> assertEquals( e.getTask().getName(), name ) );
    handler.assertNextEvent( TaskCompleteEvent.class, e -> assertEquals( e.getTask().getName(), name ) );
  }

  @Test
  public void task_different_PRIORITY()
  {
    final ArezContext context = Arez.context();

    final List<String> calls = new ArrayList<>();

    context.task( null, () -> calls.add( "1" ), Task.Flags.RUN_LATER | Task.Flags.PRIORITY_LOW );
    context.task( null, () -> calls.add( "2" ), Task.Flags.RUN_LATER | Task.Flags.PRIORITY_HIGH );
    context.task( null, () -> calls.add( "3" ), Task.Flags.RUN_LATER );
    context.task( null, () -> calls.add( "4" ), Task.Flags.RUN_LATER | Task.Flags.PRIORITY_HIGH );
    context.task( null, () -> calls.add( "5" ), Task.Flags.RUN_LATER | Task.Flags.PRIORITY_HIGHEST );
    context.task( null, () -> calls.add( "6" ), Task.Flags.RUN_LATER | Task.Flags.PRIORITY_LOWEST );
    context.task( null, () -> calls.add( "7" ), Task.Flags.RUN_LATER | Task.Flags.PRIORITY_NORMAL );

    // Trigger scheduler and allow tasks to run according to priority
    context.triggerScheduler();

    assertEquals( String.join( ",", calls ), "5,2,4,3,7,1,6" );
  }

  @SuppressWarnings( "MagicConstant" )
  @Test
  public void task_bad_flags()
  {
    final ArezContext context = Arez.context();

    assertInvariantFailure( () -> context.task( "MyTask",
                                                ValueUtil::randomString,
                                                ActionFlags.REQUIRE_NEW_TRANSACTION ),
                            "Arez-0224: Task named 'MyTask' passed invalid flags: " +
                            ActionFlags.REQUIRE_NEW_TRANSACTION );
  }

  @Test
  public void setTaskInterceptor_whenTaskInterceptorDisabled()
  {
    ArezTestUtil.disableTaskInterceptor();
    final ArezContext context = Arez.context();
    assertInvariantFailure( () -> context.setTaskInterceptor( SafeProcedure::call ),
                            "Arez-0039: setTaskInterceptor() invoked but Arez.isTaskInterceptorEnabled() returns false." );
  }

  @Test
  public void taskInterceptor()
  {
    final AtomicInteger preTaskCount = new AtomicInteger();
    final AtomicInteger postTaskCount = new AtomicInteger();
    final TaskInterceptor interceptor = a -> {
      preTaskCount.incrementAndGet();
      a.call();
      postTaskCount.incrementAndGet();
    };
    final ArezContext context = Arez.context();
    context.setTaskInterceptor( interceptor );

    assertEquals( preTaskCount.get(), 0 );
    assertEquals( postTaskCount.get(), 0 );
    context.task( () -> {
      assertEquals( preTaskCount.get(), 1 );
      assertEquals( postTaskCount.get(), 0 );
    } );
    assertEquals( preTaskCount.get(), 1 );
    assertEquals( postTaskCount.get(), 1 );
  }

  @Test
  public void taskInterceptor_whereInterceptorGeneratesMoreTasks()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger preTaskCount = new AtomicInteger();
    final AtomicInteger postTaskCount = new AtomicInteger();
    final TaskInterceptor interceptor = a -> {
      preTaskCount.incrementAndGet();
      a.call();
      if ( 0 == postTaskCount.get() )
      {
        // First time through generate another task in the interceptor
        context.task( () -> {
          final int count = preTaskCount.get();
          assertEquals( count, 2 );
          assertEquals( count + 1, postTaskCount.get() );
        } );
      }
      postTaskCount.incrementAndGet();
    };
    context.setTaskInterceptor( interceptor );

    assertEquals( preTaskCount.get(), 0 );
    assertEquals( postTaskCount.get(), 0 );
    context.task( () -> {
      final int count = preTaskCount.get();
      assertEquals( count + 1, postTaskCount.get() );
    } );
    assertEquals( preTaskCount.get(), 2 );
    assertEquals( postTaskCount.get(), 2 );
  }
}
