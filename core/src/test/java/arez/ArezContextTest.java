package arez;

import arez.component.TypeBasedLocator;
import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComponentCreateStartedEvent;
import arez.spy.ComputedValueCreatedEvent;
import arez.spy.ObservableCreatedEvent;
import arez.spy.ObserverCreatedEvent;
import arez.spy.ObserverErrorEvent;
import arez.spy.ObserverInfo;
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
  public void toName()
  {
    final ArezContext context = Arez.context();

    // Use passed in name
    assertEquals( context.generateNodeName( "ComputedValue", "MyName" ), "MyName" );

    //synthesize name
    context.setNextNodeId( 1 );
    assertEquals( context.generateNodeName( "ComputedValue", null ), "ComputedValue@1" );
    assertEquals( context.getNextNodeId(), 2 );

    ArezTestUtil.disableNames();

    //Ignore name
    assertEquals( context.generateNodeName( "ComputedValue", "MyName" ), null );

    //Null name also fine
    assertEquals( context.generateNodeName( "ComputedValue", null ), null );
  }

  @Test
  public void triggerScheduler()
  {
    final ArezContext context = Arez.context();
    final AtomicInteger callCount = new AtomicInteger();

    context.autorun( ValueUtil.randomString(), false, () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, false );

    assertEquals( callCount.get(), 0 );

    context.triggerScheduler();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void triggerScheduler_alreadyActive()
  {
    final ArezContext context = Arez.context();
    final AtomicInteger callCount = new AtomicInteger();

    context.autorun( ValueUtil.randomString(), false, () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, false );

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

    context.autorun( ValueUtil.randomString(), false, () -> {
      observeADependency();
      callCount.incrementAndGet();
      assertEquals( environment.get(), "RED" );

    }, false );

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
        context.safeAction( null, true, false, () -> observerReference.get().setState( ObserverState.STALE ) );
      }
      environment.set( null );
    } );

    final Observer observer =
      context.autorun( ValueUtil.randomString(), false, () -> {
        final Observable<Object> observable = Arez.context().observable();
        observable.reportObserved();
        callCount.incrementAndGet();
        assertEquals( environment.get(), "RED" );

      }, false );

    observerReference.set( observer );

    assertEquals( callCount.get(), 0 );
    assertEquals( environment.get(), null );

    context.triggerScheduler();

    assertEquals( callCount.get(), 3 );
    assertEquals( count.get(), 0 );
    assertEquals( environment.get(), null );
  }

  @Test
  public void isWriteTransactionActive()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isWriteTransactionActive() );

    context.action( true, () -> {
      assertTrue( context.isTransactionActive() );
      assertTrue( context.isWriteTransactionActive() );
      observeADependency();
      context.action( false, () -> {
        assertTrue( context.isTransactionActive() );
        assertFalse( context.isWriteTransactionActive() );
        observeADependency();
      } );
    } );

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isWriteTransactionActive() );
  }

  @Test
  public void isTrackingTransactionActive()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isWriteTransactionActive() );
    assertFalse( context.isTrackingTransactionActive() );

    context.action( true, () -> {
      assertTrue( context.isTransactionActive() );
      assertTrue( context.isWriteTransactionActive() );
      observeADependency();
    } );

    final Observer tracker = context.tracker( () -> assertFalse( context.isTrackingTransactionActive() ) );

    final Procedure action = () -> {
      assertTrue( context.isTransactionActive() );
      assertTrue( context.isTrackingTransactionActive() );
    };
    context.track( tracker, action );

    assertFalse( context.isTransactionActive() );
    assertFalse( context.isWriteTransactionActive() );
    assertFalse( context.isTrackingTransactionActive() );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void requireNewTransaction_false()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    context.action( ValueUtil.randomString(), true, false, true, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();

      context.action( ValueUtil.randomString(),
                      true,
                      false,
                      true,
                      () -> assertNotEquals( context.getTransaction(), transaction ) );

      final int result1 =
        context.action( ValueUtil.randomString(), true, false, true, () -> {
          assertNotEquals( context.getTransaction(), transaction );
          return 0;
        } );

      context.safeAction( ValueUtil.randomString(),
                          true,
                          false,
                          true,
                          () -> assertNotEquals( context.getTransaction(), transaction ) );

      final int result2 =
        context.safeAction( ValueUtil.randomString(), true, false, true, () -> {
          assertNotEquals( context.getTransaction(), transaction );
          return 0;
        } );

      context.action( ValueUtil.randomString(),
                      true,
                      false,
                      false,
                      () -> assertEquals( context.getTransaction(), transaction ) );

      final int result3 =
        context.action( ValueUtil.randomString(), true, false, false, () -> {
          assertEquals( context.getTransaction(), transaction );
          return 0;
        } );

      context.safeAction( ValueUtil.randomString(),
                          true,
                          false,
                          false,
                          () -> assertEquals( context.getTransaction(), transaction ) );

      final int result4 =
        context.safeAction( ValueUtil.randomString(), true, false, false, () -> {
          assertEquals( context.getTransaction(), transaction );
          return 0;
        } );
    } );
  }

  @SuppressWarnings( "unused" )
  @Test
  public void nestedAction_allowed()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger updateCalled = new AtomicInteger();

    final Observer tracker =
      context.tracker( null, null, true, updateCalled::incrementAndGet, Priority.NORMAL, false, true );

    context.track( tracker, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();

      context.action( ValueUtil.randomString(),
                      true,
                      false,
                      true,
                      () -> assertNotEquals( context.getTransaction(), transaction ) );

      final int result1 =
        context.action( ValueUtil.randomString(), true, false, true, () -> {
          assertNotEquals( context.getTransaction(), transaction );
          return 0;
        } );

      context.safeAction( ValueUtil.randomString(),
                          true,
                          false,
                          true,
                          () -> assertNotEquals( context.getTransaction(), transaction ) );

      final int result2 =
        context.safeAction( ValueUtil.randomString(), true, false, true, () -> {
          assertNotEquals( context.getTransaction(), transaction );
          return 0;
        } );

      context.action( ValueUtil.randomString(),
                      true,
                      false,
                      false,
                      () -> assertEquals( context.getTransaction(), transaction ) );
    } );
/*
    context.action( ValueUtil.randomString(), true, false, true, () -> {

      final int result3 =
        context.action( ValueUtil.randomString(), true, false, false, () -> {
          assertEquals( context.getTransaction(), transaction );
          return 0;
        } );

      context.safeAction( ValueUtil.randomString(),
                          true,
                          false,
                          false,
                          () -> assertEquals( context.getTransaction(), transaction ) );

      final int result4 =
        context.safeAction( ValueUtil.randomString(), true, false, false, () -> {
          assertEquals( context.getTransaction(), transaction );
          return 0;
        } );
    } );*/
  }

  @SuppressWarnings( "unused" )
  @Test
  public void nestedAction_notAllowed()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger updateCalled = new AtomicInteger();

    final Observer tracker =
      context.tracker( null, null, true, updateCalled::incrementAndGet, Priority.NORMAL, false, false );

    context.track( tracker, () -> {

      final IllegalStateException exception1 =
        expectThrows( IllegalStateException.class,
                      () -> context.action( "A1", true, false, true, AbstractArezTest::observeADependency ) );

      assertEquals( exception1.getMessage(),
                    "Arez-0187: Attempting to nest READ_WRITE action named 'A1' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );

      final IllegalStateException exception2 =
        expectThrows( IllegalStateException.class,
                      () -> context.action( "A2", true, false, true, () -> 1 ) );

      assertEquals( exception2.getMessage(),
                    "Arez-0187: Attempting to nest READ_WRITE action named 'A2' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );

      final IllegalStateException exception3 =
        expectThrows( IllegalStateException.class,
                      () -> context.safeAction( "A3", true, false, true, AbstractArezTest::observeADependency ) );

      assertEquals( exception3.getMessage(),
                    "Arez-0187: Attempting to nest READ_WRITE action named 'A3' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );

      final IllegalStateException exception4 =
        expectThrows( IllegalStateException.class,
                      () -> context.safeAction( "A4", true, false, true, () -> 1 ) );

      assertEquals( exception4.getMessage(),
                    "Arez-0187: Attempting to nest READ_WRITE action named 'A4' inside transaction named 'Observer@1' created by an observer that does not allow nested actions." );
    } );
  }

  @Test
  public void action_function()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertThrowsWithMessage( context::getTransaction,
                             "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final String expectedValue = ValueUtil.randomString();

    final Observable<?> observable = newObservable( context );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final boolean mutation = false;
    final String v0 =
      context.action( name, mutation, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), name );
        assertEquals( transaction.getPrevious(), null );
        assertEquals( transaction.getContext(), context );
        assertEquals( transaction.getId(), nextNodeId );
        assertEquals( transaction.getMode(), TransactionMode.READ_ONLY );

        assertEquals( observable.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );

        observable.reportObserved();

        //Not tracking so no state updated
        assertEquals( observable.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );

        return expectedValue;
      }, param1, param2, param3 );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    //Observable still not updated
    assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );
    assertEquals( observable.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    {
      final ActionStartedEvent e = handler.assertEvent( ActionStartedEvent.class, 0 );
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    }
    {
      final TransactionStartedEvent e = handler.assertEvent( TransactionStartedEvent.class, 1 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final ActionCompletedEvent e = handler.assertEvent( ActionCompletedEvent.class, 3 );
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
    }
  }

  @Test
  public void action_function_throwsException()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertThrowsWithMessage( context::getTransaction,
                             "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final String name = ValueUtil.randomString();

    final IOException ioException = new IOException();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final boolean mutation = false;
    assertThrows( IOException.class, () ->
      context.action( name, mutation, () -> {
        throw ioException;
      }, param1, param2, param3 ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    {
      final ActionStartedEvent e = handler.assertEvent( ActionStartedEvent.class, 0 );
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    }
    {
      final TransactionStartedEvent e = handler.assertEvent( TransactionStartedEvent.class, 1 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final ActionCompletedEvent e = handler.assertEvent( ActionCompletedEvent.class, 3 );
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
    }
  }

  @Test
  public void action_function_NameButNoMutationVariant()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    context.action( name, () -> {
      observeADependency();
      assertEquals( context.getTransaction().getMode(), TransactionMode.READ_WRITE );
      assertEquals( context.getTransaction().getName(), name );
      return ValueUtil.randomString();
    } );
  }

  @Test
  public void action_procedure_verifyActionRequired_false()
    throws Throwable
  {
    Arez.context().action( ValueUtil.randomString(), false, false, (Procedure) ValueUtil::randomString );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_procedure_verifyActionRequired_true_butInvariantsDisabled()
    throws Throwable
  {
    ArezTestUtil.noCheckInvariants();

    Arez.context().action( ValueUtil.randomString(), false, true, (Procedure) ValueUtil::randomString );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_procedure_verifyActionRequired_true()
    throws Throwable
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Arez.context().action( "X", false, true, (Procedure) ValueUtil::randomString ) );
    assertEquals( exception.getMessage(),
                  "Arez-0185: Action named 'X' completed but no reads or writes occurred within the scope of the action." );
  }

  @Test
  public void action_procedure_verifyActionRequired_true_is_default()
    throws Throwable
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Arez.context().action( "X", (Procedure) ValueUtil::randomString ) );
    assertEquals( exception.getMessage(),
                  "Arez-0185: Action named 'X' completed but no reads or writes occurred within the scope of the action." );
  }

  @Test
  public void action_function_verifyActionRequired_false()
    throws Throwable
  {
    Arez.context().action( ValueUtil.randomString(), false, false, (Function<String>) ValueUtil::randomString );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_function_verifyActionRequired_true_butInvariantsDisabled()
    throws Throwable
  {
    ArezTestUtil.noCheckInvariants();

    Arez.context().action( ValueUtil.randomString(), false, true, (Function<String>) ValueUtil::randomString );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void action_function_verifyActionRequired_true()
    throws Throwable
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Arez.context().action( "X", false, true, (Function<String>) ValueUtil::randomString ) );
    assertEquals( exception.getMessage(),
                  "Arez-0185: Action named 'X' completed but no reads or writes occurred within the scope of the action." );
  }

  @Test
  public void action_function_verifyActionRequired_true_is_default()
    throws Throwable
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Arez.context().action( "X", (Function<String>) ValueUtil::randomString ) );
    assertEquals( exception.getMessage(),
                  "Arez-0185: Action named 'X' completed but no reads or writes occurred within the scope of the action." );
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_false()
    throws Throwable
  {
    Arez.context().safeAction( ValueUtil.randomString(), false, false, (SafeProcedure) ValueUtil::randomString );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_true_butInvariantsDisabled()
    throws Throwable
  {
    ArezTestUtil.noCheckInvariants();

    Arez.context().safeAction( ValueUtil.randomString(), false, true, (SafeProcedure) ValueUtil::randomString );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_true()
    throws Throwable
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Arez.context().safeAction( "X", false, true, (SafeProcedure) ValueUtil::randomString ) );
    assertEquals( exception.getMessage(),
                  "Arez-0185: Action named 'X' completed but no reads or writes occurred within the scope of the action." );
  }

  @Test
  public void safeAction_procedure_verifyActionRequired_true_is_default()
    throws Throwable
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Arez.context().safeAction( "X", (SafeProcedure) ValueUtil::randomString ) );
    assertEquals( exception.getMessage(),
                  "Arez-0185: Action named 'X' completed but no reads or writes occurred within the scope of the action." );
  }

  @Test
  public void safeAction_function_verifyActionRequired_false()
    throws Throwable
  {
    Arez.context().safeAction( ValueUtil.randomString(), false, false, (SafeFunction<String>) ValueUtil::randomString );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_function_verifyActionRequired_true_butInvariantsDisabled()
    throws Throwable
  {
    ArezTestUtil.noCheckInvariants();

    Arez.context().safeAction( ValueUtil.randomString(), false, true, (SafeFunction<String>) ValueUtil::randomString );
    // If we get to here then we performed an action where no read or write occurred
  }

  @Test
  public void safeAction_function_verifyActionRequired_true()
    throws Throwable
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Arez.context()
                      .safeAction( "X", false, true, (SafeFunction<String>) ValueUtil::randomString ) );
    assertEquals( exception.getMessage(),
                  "Arez-0185: Action named 'X' completed but no reads or writes occurred within the scope of the action." );
  }

  @Test
  public void safeAction_function_verifyActionRequired_true_is_default()
    throws Throwable
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Arez.context().safeAction( "X", (SafeFunction<String>) ValueUtil::randomString ) );
    assertEquals( exception.getMessage(),
                  "Arez-0185: Action named 'X' completed but no reads or writes occurred within the scope of the action." );
  }

  @Test
  public void action_function_minimalParameters()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();

    final Observable<Object> observable = context.observable();
    final int nextNodeId = context.getNextNodeId();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String v0 =
      context.action( () -> {
        observable.reportObserved();
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), "Transaction@" + nextNodeId );
        assertEquals( transaction.getMode(), TransactionMode.READ_WRITE );

        return expectedValue;
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    handler.assertEventCount( 4 );

    {
      final ActionStartedEvent e = handler.assertEvent( ActionStartedEvent.class, 0 );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 0 );
    }
    {
      final TransactionStartedEvent e = handler.assertEvent( TransactionStartedEvent.class, 1 );
      assertEquals( e.isMutation(), true );
      assertEquals( e.getTracker(), null );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.isMutation(), true );
      assertEquals( e.getTracker(), null );
    }
    {
      final ActionCompletedEvent e = handler.assertEvent( ActionCompletedEvent.class, 3 );
      assertEquals( e.getThrowable(), null );
      assertEquals( e.returnsResult(), true );
      assertEquals( e.getResult(), v0 );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 0 );
    }
  }

  @Test
  public void track_function()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer tracker = context.tracker( true, callCount::incrementAndGet );

    final Observable<?> observable = newObservable( context );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String v0 =
      context.track( tracker, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), tracker.getName() );
        assertEquals( transaction.getMode(), tracker.getMode() );

        assertEquals( observable.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );

        observable.reportObserved();

        // Tracking so state updated
        final ArrayList<Observable<?>> observables = transaction.getObservables();
        assertNotNull( observables );
        assertEquals( observables.size(), 1 );
        assertEquals( observable.getObservers().size(), 0 );
        assertEquals( observable.getLastTrackerTransactionId(), nextNodeId );

        return expectedValue;
      }, param1, param2, param3 );

    assertFalse( context.isTransactionActive() );
    context.getSpy().removeSpyEventHandler( handler );

    assertEquals( v0, expectedValue );

    assertEquals( observable.getLastTrackerTransactionId(), 0 );
    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );

    // Reaction not called as the function sets up initial tracking
    assertEquals( callCount.get(), 0 );

    context.action( observable::reportChanged );

    assertEquals( callCount.get(), 1 );
    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );

    handler.assertEventCount( 4 );

    {
      final ActionStartedEvent e = handler.assertEvent( ActionStartedEvent.class, 0 );
      assertEquals( e.isTracked(), true );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    }
    {
      final TransactionStartedEvent e = handler.assertEvent( TransactionStartedEvent.class, 1 );
      assertEquals( e.isMutation(), true );
      final ObserverInfo info = e.getTracker();
      assertNotNull( info );
      assertEquals( info.getName(), tracker.getName() );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.isMutation(), true );
      final ObserverInfo info = e.getTracker();
      assertNotNull( info );
      assertEquals( info.getName(), tracker.getName() );
    }
    {
      final ActionCompletedEvent e = handler.assertEvent( ActionCompletedEvent.class, 3 );
      assertEquals( e.getThrowable(), null );
      assertEquals( e.returnsResult(), true );
      assertEquals( e.getResult(), v0 );
      assertEquals( e.isTracked(), true );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    }
  }

  @Test
  public void track_function_passingNonTracker()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = newObserver( context );

    assertThrowsWithMessage( () -> context.track( observer, callCount::incrementAndGet ),
                             "Arez-0017: Attempted to track Observer named '" + observer.getName() +
                             "' but observer is not a tracker." );

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void action_safeFunction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertThrowsWithMessage( context::getTransaction,
                             "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final String expectedValue = ValueUtil.randomString();

    final Observable<?> observable = newObservable( context );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final boolean mutation = false;
    final String v0 =
      context.safeAction( name, mutation, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), name );
        assertEquals( transaction.getPrevious(), null );
        assertEquals( transaction.getContext(), context );
        assertEquals( transaction.getId(), nextNodeId );
        assertEquals( transaction.getMode(), TransactionMode.READ_ONLY );

        assertEquals( observable.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );

        observable.reportObserved();

        //Not tracking so no state updated
        assertEquals( observable.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );

        return expectedValue;
      }, param1, param2, param3 );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    //Observable still not updated
    assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );
    assertEquals( observable.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    {
      final ActionStartedEvent e = handler.assertEvent( ActionStartedEvent.class, 0 );
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    }
    {
      final TransactionStartedEvent e = handler.assertEvent( TransactionStartedEvent.class, 1 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final ActionCompletedEvent e = handler.assertEvent( ActionCompletedEvent.class, 3 );
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
    }
  }

  @Test
  public void action_safeFunction_throws_Exception()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertThrowsWithMessage( context::getTransaction,
                             "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final AccessControlException secException = new AccessControlException( "" );

    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final boolean mutation = false;

    assertThrows( AccessControlException.class, () ->
      context.safeAction( name, mutation, () -> {
        throw secException;
      }, param1, param2, param3 ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    {
      final ActionStartedEvent e = handler.assertEvent( ActionStartedEvent.class, 0 );
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    }
    {
      final TransactionStartedEvent e = handler.assertEvent( TransactionStartedEvent.class, 1 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final ActionCompletedEvent e = handler.assertEvent( ActionCompletedEvent.class, 3 );
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
    }
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
        assertEquals( transaction.getName(), "Transaction@" + nextNodeId );
        assertEquals( transaction.getMode(), TransactionMode.READ_WRITE );
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
      assertEquals( context.getTransaction().getMode(), TransactionMode.READ_WRITE );
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

    final Observable<?> observable = newObservable( context );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();

    final String v0 =
      context.safeTrack( tracker, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction = context.getTransaction();
        assertEquals( transaction.getName(), tracker.getName() );
        assertEquals( transaction.getMode(), tracker.getMode() );

        assertEquals( observable.getObservers().size(), 0 );
        assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );

        observable.reportObserved();

        // Tracking so state updated
        final ArrayList<Observable<?>> observables = transaction.getObservables();
        assertNotNull( observables );
        assertEquals( observables.size(), 1 );
        assertEquals( observable.getObservers().size(), 0 );
        assertEquals( observable.getLastTrackerTransactionId(), nextNodeId );

        return expectedValue;
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    assertEquals( observable.getLastTrackerTransactionId(), 0 );
    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );

    // Reaction not called as the function sets up initial tracking
    assertEquals( callCount.get(), 0 );

    context.action( observable::reportChanged );

    assertEquals( callCount.get(), 1 );
    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );
  }

  @Test
  public void track_safeFunction_passingNonTracker()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = newObserver( context );

    assertThrowsWithMessage( () -> context.safeTrack( observer, callCount::incrementAndGet ),
                             "Arez-0018: Attempted to track Observer named '" + observer.getName() +
                             "' but observer is not a tracker." );

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
      assertEquals( context.getTransaction().getMode(), TransactionMode.READ_WRITE );
      assertEquals( context.getTransaction().getName(), "Transaction@" + nextNodeId );
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
      assertEquals( context.getTransaction().getMode(), TransactionMode.READ_WRITE );
      assertEquals( context.getTransaction().getName(), name );
    } );
  }

  @Test
  public void action_safeProcedure_throws_Exception()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    assertThrowsWithMessage( context::getTransaction,
                             "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final AccessControlException secException = new AccessControlException( "" );

    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final boolean mutation = false;

    final SafeProcedure procedure = () -> {
      throw secException;
    };
    assertThrows( AccessControlException.class,
                  () -> context.safeAction( name, mutation, procedure, param1, param2, param3 ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    {
      final ActionStartedEvent e = handler.assertEvent( ActionStartedEvent.class, 0 );
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    }
    {
      final TransactionStartedEvent e = handler.assertEvent( TransactionStartedEvent.class, 1 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final ActionCompletedEvent e = handler.assertEvent( ActionCompletedEvent.class, 3 );
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
    }
  }

  @Test
  public void track_safeProcedure()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final AtomicInteger callCount = new AtomicInteger();

    final Observer tracker = context.tracker( callCount::incrementAndGet );

    final Observable<?> observable = newObservable( context );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();

    context.safeTrack( tracker, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), tracker.getName() );
      assertEquals( transaction.getMode(), tracker.getMode() );

      assertEquals( observable.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );

      observable.reportObserved();

      // Tracking so state updated
      final ArrayList<Observable<?>> observables = transaction.getObservables();
      assertNotNull( observables );
      assertEquals( observables.size(), 1 );
      assertEquals( observable.getObservers().size(), 0 );
      assertEquals( observable.getLastTrackerTransactionId(), nextNodeId );
    } );

    assertFalse( context.isTransactionActive() );

    assertEquals( observable.getLastTrackerTransactionId(), 0 );
    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );

    // Reaction not called as the function sets up initial tracking
    assertEquals( callCount.get(), 0 );

    context.action( observable::reportChanged );

    assertEquals( callCount.get(), 1 );
    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );
  }

  @Test
  public void track_safeProcedure_passingNonTracker()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = newObserver( context );

    final SafeProcedure procedure = callCount::incrementAndGet;
    assertThrowsWithMessage( () -> context.safeTrack( observer, procedure ),
                             "Arez-0020: Attempted to track Observer named '" + observer.getName() +
                             "' but observer is not a tracker." );

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
      assertEquals( context.getTransaction().getMode(), TransactionMode.READ_WRITE );
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
      assertEquals( context.getTransaction().getMode(), TransactionMode.READ_WRITE );
      assertEquals( context.getTransaction().getName(), "Transaction@" + nextNodeId );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void track_procedure_passingNonTracker()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = newObserver( context );

    final Procedure procedure = callCount::incrementAndGet;
    assertThrowsWithMessage( () -> context.track( observer, procedure ),
                             "Arez-0019: Attempted to track Observer named '" +
                             observer.getName() + "' but observer is not a tracker." );

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

    final Observable<?> observable = newObservable( context );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();

    context.track( tracker, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), tracker.getName() );
      assertEquals( transaction.getMode(), tracker.getMode() );

      assertEquals( observable.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );

      observable.reportObserved();

      // Tracking so state updated
      final ArrayList<Observable<?>> observables = transaction.getObservables();
      assertNotNull( observables );
      assertEquals( observables.size(), 1 );
      assertEquals( observable.getObservers().size(), 0 );
      assertEquals( observable.getLastTrackerTransactionId(), nextNodeId );
    } );

    assertFalse( context.isTransactionActive() );

    assertEquals( observable.getLastTrackerTransactionId(), 0 );
    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );

    // Reaction not called as the function sets up initial tracking
    assertEquals( callCount.get(), 0 );

    context.action( observable::reportChanged );

    assertEquals( callCount.get(), 1 );
    assertEquals( observable.getObservers().size(), 1 );
    assertEquals( tracker.getDependencies().size(), 1 );
  }

  @Test
  public void nonTrackingSafeProcedureObservingSingleObservable()
    throws Exception
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertThrowsWithMessage( context::getTransaction,
                             "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final Observable<?> observable = newObservable( context );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();
    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final boolean mutation = false;

    context.safeAction( name, mutation, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), name );
      assertEquals( transaction.getPrevious(), null );
      assertEquals( transaction.getContext(), context );
      assertEquals( transaction.getId(), nextNodeId );

      assertEquals( observable.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );

      observable.reportObserved();

      //Not tracking so no state updated
      assertEquals( observable.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );
    }, param1, param2, param3 );

    assertFalse( context.isTransactionActive() );

    //Observable still not updated
    assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );
    assertEquals( observable.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    {
      final ActionStartedEvent e = handler.assertEvent( ActionStartedEvent.class, 0 );
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    }
    {
      final TransactionStartedEvent e = handler.assertEvent( TransactionStartedEvent.class, 1 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final ActionCompletedEvent e = handler.assertEvent( ActionCompletedEvent.class, 3 );
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
    }
  }

  @Test
  public void action_procedure()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertThrowsWithMessage( context::getTransaction,
                             "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final Observable<?> observable = newObservable( context );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final Observable<Object> observable1 = Arez.context().observable();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final boolean mutation = false;
    context.action( name, mutation, () -> {
      observable1.reportObserved();
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), name );
      assertEquals( transaction.getPrevious(), null );
      assertEquals( transaction.getContext(), context );
      assertEquals( transaction.getId(), nextNodeId );

      assertEquals( observable.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );

      observable.reportObserved();

      //Not tracking so no state updated
      assertEquals( observable.getObservers().size(), 0 );
      assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );
    }, param1, param2, param3 );

    assertFalse( context.isTransactionActive() );

    //Observable still not updated
    assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );
    assertEquals( observable.getObservers().size(), 0 );

    handler.assertEventCount( 4 );

    {
      final ActionStartedEvent e = handler.assertEvent( ActionStartedEvent.class, 0 );
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    }
    {
      final TransactionStartedEvent e = handler.assertEvent( TransactionStartedEvent.class, 1 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final ActionCompletedEvent e = handler.assertEvent( ActionCompletedEvent.class, 3 );
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
    }
  }

  @Test
  public void action_procedure_throwsException()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );
    assertThrowsWithMessage( context::getTransaction,
                             "Arez-0117: Attempting to get current transaction but no transaction is active." );

    final String name = ValueUtil.randomString();
    final IOException ioException = new IOException();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final Observable observable = Arez.context().observable();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final boolean mutation = false;
    final Procedure procedure = () -> {
      observable.reportObserved();
      throw ioException;
    };
    assertThrows( IOException.class, () -> context.action( name, mutation, procedure, param1, param2, param3 ) );

    assertFalse( context.isTransactionActive() );

    handler.assertEventCount( 4 );

    {
      final ActionStartedEvent e = handler.assertEvent( ActionStartedEvent.class, 0 );
      assertEquals( e.getName(), name );
      assertEquals( e.isTracked(), false );
      final Object[] parameters = e.getParameters();
      assertEquals( parameters.length, 3 );
      assertEquals( parameters[ 0 ], param1 );
      assertEquals( parameters[ 1 ], param2 );
      assertEquals( parameters[ 2 ], param3 );
    }
    {
      final TransactionStartedEvent e = handler.assertEvent( TransactionStartedEvent.class, 1 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.getName(), name );
      assertEquals( e.isMutation(), mutation );
      assertEquals( e.getTracker(), null );
    }
    {
      final ActionCompletedEvent e = handler.assertEvent( ActionCompletedEvent.class, 3 );
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
    }
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
    context.action( name, false, () -> {
      observeADependency();
      assertTrue( context.isTransactionActive() );
      final Transaction transaction1 = context.getTransaction();
      assertEquals( transaction1.getName(), name );
      assertEquals( transaction1.getPrevious(), null );
      assertEquals( transaction1.getContext(), context );
      assertEquals( transaction1.getId(), nextNodeId );
      assertEquals( transaction1.isRootTransaction(), true );
      assertEquals( transaction1.getRootTransaction(), transaction1 );

      context.action( name2, false, () -> {
        observeADependency();
        assertTrue( context.isTransactionActive() );
        final Transaction transaction2 = context.getTransaction();
        assertEquals( transaction2.getName(), name2 );
        assertEquals( transaction2.getPrevious(), transaction1 );
        assertEquals( transaction2.getContext(), context );
        assertEquals( transaction2.getId(), nextNodeId + 1 );
        assertEquals( transaction2.isRootTransaction(), false );
        assertEquals( transaction2.getRootTransaction(), transaction1 );
      } );

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
    final ArezContext context = new ArezContext();

    final ObserverError observerError = ObserverError.REACTION_ERROR;
    final Throwable throwable = new Throwable();
    final Procedure action = new NoopProcedure();
    final Observer observer = context.autorun( ValueUtil.randomString(), true, action );

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
    final ArezContext context = new ArezContext();

    final ObserverError observerError = ObserverError.REACTION_ERROR;
    final Throwable throwable = new Throwable();
    final Procedure action = new NoopProcedure();
    final Observer observer =
      context.autorun( ValueUtil.randomString(), ValueUtil.randomBoolean(), action, ValueUtil.randomBoolean() );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    context.reportObserverError( observer, observerError, throwable );

    handler.assertEventCount( 1 );

    final ObserverErrorEvent event = handler.assertEvent( ObserverErrorEvent.class, 0 );

    assertEquals( event.getObserver().getName(), observer.getName() );
    assertEquals( event.getError(), observerError );
    assertEquals( event.getThrowable(), throwable );
  }

  @Test
  public void addObserverErrorHandler_whenDisabled()
    throws Exception
  {
    ArezTestUtil.disableObserverErrorHandlers();

    final ObserverErrorHandler handler = ( o, e, t ) -> {
    };

    assertThrowsWithMessage( () -> Arez.context().addObserverErrorHandler( handler ),
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

    assertThrowsWithMessage( () -> context.removeObserverErrorHandler( handler ),
                             "Arez-0181: ArezContext.removeObserverErrorHandler() invoked when Arez.areObserverErrorHandlersEnabled() returns false." );
  }

  @Test
  public void getSpy_whenSpiesDisabled()
    throws Exception
  {
    ArezTestUtil.disableSpies();

    final ArezContext context = Arez.context();

    assertThrowsWithMessage( context::getSpy, "Arez-0021: Attempting to get Spy but spies are not enabled." );
  }

  @Test
  public void scheduleReaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer = newReadOnlyObserver( context );

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

    final Observer observer = newReadOnlyObserver( context );

    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    assertThrowsWithMessage( () -> context.action( false, () -> context.scheduleReaction( observer ) ),
                             "Arez-0013: Observer named '" + observer.getName() + "' attempted to be scheduled " +
                             "during read-only transaction." );
  }

  @Test
  public void scheduleReaction_shouldAbortInReadWriteOwnedTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer derivation = newDerivation();

    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    setCurrentTransaction( derivation );

    assertThrowsWithMessage( () -> context.scheduleReaction( derivation ),
                             "Arez-0014: Observer named '" + derivation.getName() + "' attempted to schedule itself " +
                             "during read-only tracking transaction. Observers that are supporting ComputedValue " +
                             "instances must not schedule self." );
  }

  @Test
  public void scheduleReaction_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer = newReadOnlyObserver( context );

    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );
    context.scheduleReaction( observer );

    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );

    handler.assertEventCount( 1 );
    final ReactionScheduledEvent event = handler.assertEvent( ReactionScheduledEvent.class, 0 );
    assertEquals( event.getObserver().getName(), observer.getName() );
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
    final Procedure onDispose = ValueUtil::randomString;
    final ComputedValue<String> computedValue =
      context.computedValue( null,
                             name,
                             function,
                             onActivate,
                             onDeactivate,
                             onStale,
                             onDispose,
                             Priority.HIGH );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.isKeepAlive(), false );
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObservable().getName(), name );
    assertEquals( computedValue.getObserver().getOnActivate(), onActivate );
    assertEquals( computedValue.getObserver().getOnDeactivate(), onDeactivate );
    assertEquals( computedValue.getObserver().getOnStale(), onStale );
    assertEquals( computedValue.getObserver().getOnDispose(), onDispose );
    assertEquals( computedValue.getObserver().getPriority(), Priority.HIGH );
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
      context.computedValue( component, name, () -> "", null, null, null, null );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getComponent(), component );
  }

  @Test
  public void computedValue_canObserveLowerPriorityDependencies()
    throws Exception
  {
    final ComputedValue<String> computedValue =
      Arez.context().computedValue( null,
                                    ValueUtil.randomString(),
                                    () -> "",
                                    null,
                                    null,
                                    null,
                                    null,
                                    Priority.NORMAL,
                                    false,
                                    false,
                                    true );

    assertEquals( computedValue.getObserver().canObserveLowerPriorityDependencies(), true );
  }

  @Test
  public void computedValue_withKeepAliveAndRunImmediately()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> action = () -> {
      observeADependency();
      calls.incrementAndGet();
      return "";
    };
    final ComputedValue<String> computedValue =
      context.computedValue( null,
                             name,
                             action,
                             null,
                             null,
                             null,
                             null,
                             Priority.NORMAL,
                             true,
                             true );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.isKeepAlive(), true );
    assertEquals( calls.get(), 1 );
  }

  @Test
  public void computedValue_withKeepAliveAndNoRunImmediately()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> action = () -> {
      observeADependency();
      calls.incrementAndGet();
      return "";
    };
    final ComputedValue<String> computedValue =
      context.computedValue( null,
                             name,
                             action,
                             null,
                             null,
                             null,
                             null,
                             Priority.NORMAL,
                             true,
                             false );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.isKeepAlive(), true );
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
    final ComputedValue<String> computedValue = context.computedValue( name, function );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObservable().getName(), name );
    assertEquals( computedValue.getObserver().getOnActivate(), null );
    assertEquals( computedValue.getObserver().getOnDeactivate(), null );
    assertEquals( computedValue.getObserver().getOnStale(), null );
    assertEquals( computedValue.getObserver().getOnDispose(), null );
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
    final ComputedValue<String> computedValue = context.computedValue( function );

    final String name = "ComputedValue@22";
    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObservable().getName(), name );
    assertEquals( computedValue.getObserver().getOnActivate(), null );
    assertEquals( computedValue.getObserver().getOnDeactivate(), null );
    assertEquals( computedValue.getObserver().getOnStale(), null );
    assertEquals( computedValue.getObserver().getOnDeactivate(), null );
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
      context.computedValue( ValueUtil.randomString(), () -> {
        observeADependency();
        return "";
      } );

    handler.assertEventCount( 1 );

    final ComputedValueCreatedEvent event = handler.assertEvent( ComputedValueCreatedEvent.class, 0 );
    assertEquals( event.getComputedValue().getName(), computedValue.getName() );
  }

  @Test
  public void computedValue_withKeepAliveAndOnActivate()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Procedure action = () -> {
    };
    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.computedValue( null,
                                                 ValueUtil.randomString(),
                                                 function,
                                                 action,
                                                 null,
                                                 null,
                                                 null,
                                                 Priority.NORMAL,
                                                 true,
                                                 true ) );
    assertEquals( exception.getMessage(),
                  "Arez-0039: ArezContext.computedValue() specified keepAlive = true and did not pass a null for onActivate." );
  }

  @Test
  public void computedValue_withKeepAliveAndOnDeactivate()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Procedure action = AbstractArezTest::observeADependency;
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.computedValue( null,
                                                 ValueUtil.randomString(),
                                                 () -> "",
                                                 null,
                                                 action,
                                                 null,
                                                 null,
                                                 Priority.NORMAL,
                                                 true,
                                                 true ) );
    assertEquals( exception.getMessage(),
                  "Arez-0045: ArezContext.computedValue() specified keepAlive = true and did not pass a null for onDeactivate." );
  }

  @Test
  public void autorun_noObservers()
    throws Exception
  {
    setPrintObserverErrors( false );
    setIgnoreObserverErrors( true );

    Arez.context().setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = Arez.context().autorun( callCount::incrementAndGet );

    assertEquals( observer.getName(), "Observer@22" );
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( callCount.get(), 1 );

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( getObserverErrors().get( 0 ),
                  "Observer: Observer@22 Error: REACTION_ERROR java.lang.IllegalStateException: Arez-0172: Autorun observer named 'Observer@22' completed reaction but is not observing any observables and thus will never be rescheduled. This may not be an autorun candidate." );
  }

  @Test
  public void autorun_minimumParameters()
    throws Exception
  {
    final ArezContext context = Arez.context();

    context.setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.autorun( () -> {
      observeADependency();
      callCount.incrementAndGet();
    } );

    assertEquals( observer.getName(), "Observer@22" );
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.canObserveLowerPriorityDependencies(), false );
    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void autorun_withComponent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final AtomicInteger callCount = new AtomicInteger();
    final String name = ValueUtil.randomString();
    final Observer observer = context.autorun( component, name, true, () -> {
      observeADependency();
      callCount.incrementAndGet();
    } );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getComponent(), component );
    assertEquals( observer.getPriority(), Priority.NORMAL );

    // Those created with components are not runImmediately
    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void autorun_minimumParametersForMutation()
    throws Exception
  {
    final ArezContext context = Arez.context();

    context.setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.autorun( true, () -> {
      observeADependency();
      callCount.incrementAndGet();
    } );

    assertEquals( observer.getName(), "Observer@22" );
    assertEquals( observer.getMode(), TransactionMode.READ_WRITE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.canNestActions(), false );
    assertEquals( callCount.get(), 1 );
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void autorun_runImmediately()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observable<Object> observable = Arez.context().observable();
    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.autorun( name, true, () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    }, true );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_WRITE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( callCount.get(), 1 );

    handler.assertEventCount( 7 );

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver().getName(), name );
    assertEquals( handler.assertEvent( ReactionStartedEvent.class, 1 ).getObserver().getName(), name );
    assertEquals( handler.assertEvent( ActionStartedEvent.class, 2 ).getName(), name );
    assertEquals( handler.assertEvent( TransactionStartedEvent.class, 3 ).getTracker().getName(), name );
    assertEquals( handler.assertEvent( TransactionCompletedEvent.class, 4 ).getTracker().getName(), name );
    assertEquals( handler.assertEvent( ActionCompletedEvent.class, 5 ).getName(), name );
    assertEquals( handler.assertEvent( ReactionCompletedEvent.class, 6 ).getObserver().getName(), name );
  }

  @Test
  public void autorun_highPriority()
    throws Exception
  {
    final Observer observer =
      Arez.context()
        .autorun( null, ValueUtil.randomString(), true, AbstractArezTest::observeADependency, Priority.HIGH, false );

    assertEquals( observer.getPriority(), Priority.HIGH );
  }

  @Test
  public void autorun_canObserveLowerPriorityDependencies()
    throws Exception
  {
    final Observer observer =
      Arez.context().autorun( null,
                              ValueUtil.randomString(),
                              true,
                              AbstractArezTest::observeADependency,
                              Priority.HIGH,
                              false,
                              true );

    assertEquals( observer.canObserveLowerPriorityDependencies(), true );
  }

  @Test
  public void autorun_canNestActions()
    throws Exception
  {
    final boolean canNestActions = true;
    final Observer observer =
      Arez.context().autorun( null,
                              ValueUtil.randomString(),
                              ValueUtil.randomBoolean(),
                              AbstractArezTest::observeADependency,
                              Priority.NORMAL,
                              ValueUtil.randomBoolean(),
                              ValueUtil.randomBoolean(),
                              canNestActions );

    assertEquals( observer.canNestActions(), canNestActions );
  }

  @Test
  public void autorun_disposeHook()
    throws Exception
  {
    final AtomicInteger disposeHookCallCount = new AtomicInteger();
    final Observer observer =
      Arez.context().autorun( null,
                              ValueUtil.randomString(),
                              ValueUtil.randomBoolean(),
                              AbstractArezTest::observeADependency,
                              Priority.NORMAL,
                              ValueUtil.randomBoolean(),
                              ValueUtil.randomBoolean(),
                              ValueUtil.randomBoolean(),
                              disposeHookCallCount::incrementAndGet );


    assertEquals( disposeHookCallCount.get(), 0 );

    Disposable.dispose( observer );

    assertEquals( disposeHookCallCount.get(), 1 );

    Disposable.dispose( observer );

    assertEquals( disposeHookCallCount.get(), 1 );
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
    final Observer observer = context.autorun( name, false, () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, false );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );

    handler.assertEventCount( 2 );

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver().getName(), observer.getName() );
    assertEquals( handler.assertEvent( ReactionScheduledEvent.class, 1 ).getObserver().getName(), observer.getName() );
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
    final Observer observer = context.tracker( null, name, false, callCount::incrementAndGet, Priority.HIGH );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.getComponent(), null );
    assertEquals( observer.getPriority(), Priority.HIGH );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    handler.assertEventCount( 1 );

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver().getName(), observer.getName() );
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
    final Observer observer = context.tracker( component, name, false, callCount::incrementAndGet );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getComponent(), component );
    assertEquals( observer.getPriority(), Priority.NORMAL );
    assertEquals( observer.canObserveLowerPriorityDependencies(), false );
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
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    handler.assertEventCount( 1 );

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver().getName(), observer.getName() );
  }

  @Test
  public void createObserver_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final Observer observer =
      context.observer( null,
                        ValueUtil.randomString(),
                        true,
                        new TestReaction(),
                        Priority.NORMAL,
                        false,
                        false,
                        false );

    handler.assertEventCount( 1 );

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver().getName(), observer.getName() );
  }

  @Test
  public void createObserver_canTrackExplicitly()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer =
      context.observer( null,
                        ValueUtil.randomString(),
                        false,
                        new TestReaction(),
                        Priority.NORMAL,
                        true,
                        false,
                        false );

    assertEquals( observer.canTrackExplicitly(), true );
  }

  @Test
  public void createObserver_canObserveLowerPriorityDependencies()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer =
      context.observer( null, ValueUtil.randomString(), false, new TestReaction(), Priority.NORMAL, true, true, false );

    assertEquals( observer.canObserveLowerPriorityDependencies(), true );
  }

  @Test
  public void createObservable_no_parameters()
    throws Exception
  {
    final ArezContext context = Arez.context();

    context.setNextNodeId( 22 );
    final Observable<?> observable = context.observable();

    assertNotNull( observable.getName() );
    assertEquals( observable.getName(), "Observable@22" );
    assertNull( observable.getAccessor() );
    assertNull( observable.getMutator() );
  }

  @Test
  public void createObservable()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final Observable<?> observable = context.observable( name );

    assertEquals( observable.getName(), name );
    assertEquals( observable.getAccessor(), null );
    assertEquals( observable.getMutator(), null );
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
    final Observable<?> observable = context.observable( name, accessor, mutator );

    assertEquals( observable.getName(), name );
    assertEquals( observable.getAccessor(), accessor );
    assertEquals( observable.getMutator(), mutator );
  }

  @Test
  public void createObservable_withComponent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final Observable<String> observable = context.observable( component, name );

    assertEquals( observable.getName(), name );
    assertEquals( observable.getComponent(), component );
  }

  @Test
  public void createObservable_spyEventHandlerPresent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final Observable<?> observable = context.observable( name );

    assertEquals( observable.getName(), name );
    handler.assertEventCount( 1 );
    final ObservableCreatedEvent event = handler.assertEvent( ObservableCreatedEvent.class, 0 );
    assertEquals( event.getObservable().getName(), observable.getName() );
  }

  @Test
  public void createObservable_name_Null()
    throws Exception
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();

    final Observable<?> observable = context.observable( null );

    assertNotNull( observable );
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
    context.autorun( ValueUtil.randomString(), false, () -> {
      observeADependency();
      callCount.incrementAndGet();
    }, false );
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
    assertThrowsWithMessage( () -> Arez.context().releaseSchedulerLock(),
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
    final ComponentCreateStartedEvent event = handler.assertEvent( ComponentCreateStartedEvent.class, 0 );
    assertEquals( event.getComponent(), component );
  }

  @Test
  public void createComponent_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();
    final String name = ValueUtil.randomString();

    assertThrowsWithMessage( () -> context.component( type, id, name ),
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

    assertThrowsWithMessage( () -> context.component( type, id, ValueUtil.randomString() ),
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

    assertThrowsWithMessage( () -> context.isComponentPresent( type, id ),
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

    assertThrowsWithMessage( () -> context.deregisterComponent( component ),
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

    assertThrowsWithMessage( () -> context.deregisterComponent( component ),
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

    assertThrowsWithMessage( () -> context.findComponent( type, id ),
                             "Arez-0010: ArezContext.findComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllComponentsByType_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();

    assertThrowsWithMessage( () -> context.findAllComponentsByType( type ),
                             "Arez-0011: ArezContext.findAllComponentsByType() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllComponentTypes_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    assertThrowsWithMessage( context::findAllComponentTypes,
                             "Arez-0012: ArezContext.findAllComponentTypes() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void registryAccessWhenDisabled()
  {
    ArezTestUtil.disableRegistries();

    final ArezContext context = Arez.context();

    final Observable<Object> observable = context.observable();
    final ComputedValue<String> computedValue = context.computedValue( () -> "" );
    final Observer observer = context.autorun( AbstractArezTest::observeADependency );

    assertThrowsWithMessage( () -> context.registerObservable( observable ),
                             "Arez-0022: ArezContext.registerObservable invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( () -> context.deregisterObservable( observable ),
                             "Arez-0024: ArezContext.deregisterObservable invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( context::getTopLevelObservables,
                             "Arez-0026: ArezContext.getTopLevelObservables() invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( () -> context.registerObserver( observer ),
                             "Arez-0027: ArezContext.registerObserver invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( () -> context.deregisterObserver( observer ),
                             "Arez-0029: ArezContext.deregisterObserver invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( context::getTopLevelObservers,
                             "Arez-0031: ArezContext.getTopLevelObservers() invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( () -> context.registerComputedValue( computedValue ),
                             "Arez-0032: ArezContext.registerComputedValue invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( () -> context.deregisterComputedValue( computedValue ),
                             "Arez-0034: ArezContext.deregisterComputedValue invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( context::getTopLevelComputedValues,
                             "Arez-0036: ArezContext.getTopLevelComputedValues() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void observableRegistry()
  {
    final ArezContext context = Arez.context();

    final Observable<Object> observable = context.observable();

    assertEquals( context.getTopLevelObservables().size(), 1 );
    assertEquals( context.getTopLevelObservables().get( observable.getName() ), observable );

    assertThrowsWithMessage( () -> context.registerObservable( observable ),
                             "Arez-0023: ArezContext.registerObservable invoked with observable named '" +
                             observable.getName() + "' but an existing observable with that name is " +
                             "already registered." );

    assertEquals( context.getTopLevelObservables().size(), 1 );
    context.getTopLevelObservables().clear();
    assertEquals( context.getTopLevelObservables().size(), 0 );

    assertThrowsWithMessage( () -> context.deregisterObservable( observable ),
                             "Arez-0025: ArezContext.deregisterObservable invoked with observable named '" +
                             observable.getName() + "' but no observable with that name is registered." );
  }

  @Test
  public void observerRegistry()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.autorun( AbstractArezTest::observeADependency );

    assertEquals( context.getTopLevelObservers().size(), 1 );
    assertEquals( context.getTopLevelObservers().get( observer.getName() ), observer );

    assertThrowsWithMessage( () -> context.registerObserver( observer ),
                             "Arez-0028: ArezContext.registerObserver invoked with observer named '" +
                             observer.getName() + "' but an existing observer with that name is " +
                             "already registered." );

    assertEquals( context.getTopLevelObservers().size(), 1 );
    context.getTopLevelObservers().clear();
    assertEquals( context.getTopLevelObservers().size(), 0 );

    assertThrowsWithMessage( () -> context.deregisterObserver( observer ),
                             "Arez-0030: ArezContext.deregisterObserver invoked with observer named '" +
                             observer.getName() + "' but no observer with that name is registered." );
  }

  @Test
  public void computedValueRegistry()
  {
    final ArezContext context = Arez.context();

    final ComputedValue computedValue = context.computedValue( () -> "" );

    assertEquals( context.getTopLevelComputedValues().size(), 1 );
    assertEquals( context.getTopLevelComputedValues().get( computedValue.getName() ), computedValue );

    assertThrowsWithMessage( () -> context.registerComputedValue( computedValue ),
                             "Arez-0033: ArezContext.registerComputedValue invoked with computed value " +
                             "named '" + computedValue.getName() + "' but an existing computed value with that name " +
                             "is already registered." );

    assertEquals( context.getTopLevelComputedValues().size(), 1 );
    context.getTopLevelComputedValues().clear();
    assertEquals( context.getTopLevelComputedValues().size(), 0 );

    assertThrowsWithMessage( () -> context.deregisterComputedValue( computedValue ),
                             "Arez-0035: ArezContext.deregisterComputedValue invoked with computed value named '" +
                             computedValue.getName() + "' but no computed value with that name is registered." );
  }

  @Test
  public void noTxAction_function()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();
    final String v0 =
      context.action( () -> {
        observeADependency();
        assertTrue( context.isTransactionActive() );

        return context.noTxAction( () -> {
          assertFalse( context.isTransactionActive() );
          return expectedValue;
        } );
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void noTxAction_safeFunction()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();
    final String v0 =
      context.action( () -> {
        observeADependency();
        assertTrue( context.isTransactionActive() );

        return context.safeNoTxAction( () -> {
          assertFalse( context.isTransactionActive() );
          return expectedValue;
        } );
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void noTxAction_procedure()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    context.action( () -> {
      assertTrue( context.isTransactionActive() );
      observeADependency();

      context.noTxAction( () -> assertFalse( context.isTransactionActive() ) );

      assertTrue( context.isTransactionActive() );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void noTxAction_safeProcedure()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    assertFalse( context.isTransactionActive() );

    context.action( () -> {
      assertTrue( context.isTransactionActive() );
      observeADependency();

      context.safeNoTxAction( () -> assertFalse( context.isTransactionActive() ) );

      assertTrue( context.isTransactionActive() );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void scheduleDispose()
  {
    final ArezContext context = Arez.context();
    final ReactionScheduler scheduler = context.getScheduler();

    final Observer observer = newReadOnlyObserver();

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

    assertThrowsWithMessage( () -> Arez.context().locator(),
                             "Arez-0192: ArezContext.locator() invoked but Arez.areReferencesEnabled() returned false." );
  }

  @Test
  public void registerLocator_referencesDisabled()
  {
    ArezTestUtil.disableReferences();
    ArezTestUtil.resetState();

    assertThrowsWithMessage( () -> Arez.context().registerLocator( new TypeBasedLocator() ),
                             "Arez-0191: ArezContext.registerLocator invoked but Arez.areReferencesEnabled() returned false." );
  }

  private void assertThrowsWithMessage( @Nonnull final ThrowingRunnable runnable, @Nonnull final String message )
  {
    assertEquals( expectThrows( IllegalStateException.class, runnable ).getMessage(), message );
  }
}
