package arez;

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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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
    final ArezContext context = new ArezContext();

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
    final ArezContext context = new ArezContext();
    final AtomicInteger callCount = new AtomicInteger();

    context.autorun( ValueUtil.randomString(), false, callCount::incrementAndGet, false );

    assertEquals( callCount.get(), 0 );

    context.triggerScheduler();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void action_function()
    throws Throwable
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

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
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

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
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    context.action( name, () -> {
      assertEquals( context.getTransaction().getMode(), TransactionMode.READ_WRITE );
      assertEquals( context.getTransaction().getName(), name );
      return ValueUtil.randomString();
    } );
  }

  @Test
  public void action_function_minimalParameters()
    throws Throwable
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();

    final int nextNodeId = context.currentNextTransactionId();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String v0 =
      context.action( () -> {
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
    final ArezContext context = new ArezContext();

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
    final ArezContext context = new ArezContext();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = newObserver( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.track( observer, callCount::incrementAndGet ) );
    assertEquals( exception.getMessage(),
                  "Attempted to track Observer named '" + observer.getName() + "' but observer is not a tracker." );

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void action_safeFunction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

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
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

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
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final int nextNodeId = context.currentNextTransactionId();

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.safeAction( () -> {
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
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    context.safeAction( name, () -> {
      assertEquals( context.getTransaction().getMode(), TransactionMode.READ_WRITE );
      assertEquals( context.getTransaction().getName(), name );
      return ValueUtil.randomString();
    } );
  }

  @Test
  public void track_safeFunction()
    throws Throwable
  {
    final ArezContext context = new ArezContext();

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
    final ArezContext context = new ArezContext();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = newObserver( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.safeTrack( observer, callCount::incrementAndGet ) );
    assertEquals( exception.getMessage(),
                  "Attempted to track Observer named '" + observer.getName() + "' but observer is not a tracker." );

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void safeAction_safeProcedure_minimalParameters()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final int nextNodeId = context.currentNextTransactionId();
    context.safeAction( () -> {
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
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    context.safeAction( name, () -> {
      assertEquals( context.getTransaction().getMode(), TransactionMode.READ_WRITE );
      assertEquals( context.getTransaction().getName(), name );
    } );
  }

  @Test
  public void action_safeProcedure_throws_Exception()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

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
    final ArezContext context = new ArezContext();

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
    final ArezContext context = new ArezContext();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = newObserver( context );

    final SafeProcedure procedure = callCount::incrementAndGet;
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.safeTrack( observer, procedure ) );
    assertEquals( exception.getMessage(),
                  "Attempted to track Observer named '" + observer.getName() + "' but observer is not a tracker." );

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void action_procedure_NameButNoMutationVariant()
    throws Throwable
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    context.action( name, () -> {
      assertEquals( context.getTransaction().getMode(), TransactionMode.READ_WRITE );
      assertEquals( context.getTransaction().getName(), name );
    } );
  }

  @Test
  public void action_procedure_minimalParameters()
    throws Throwable
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final int nextNodeId = context.currentNextTransactionId();
    context.action( () -> {
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
    final ArezContext context = new ArezContext();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer observer = newObserver( context );

    final Procedure procedure = callCount::incrementAndGet;
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.track( observer, procedure ) );
    assertEquals( exception.getMessage(),
                  "Attempted to track Observer named '" + observer.getName() + "' but observer is not a tracker." );

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void track_procedure()
    throws Throwable
  {
    final ArezContext context = new ArezContext();

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
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

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
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

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
    context.action( name, mutation, () -> {
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
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

    final String name = ValueUtil.randomString();
    final IOException ioException = new IOException();

    final String param1 = "";
    final Object param2 = null;
    final int param3 = 3;

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final boolean mutation = false;
    final Procedure procedure = () -> {
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
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    assertThrows( context::getTransaction );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();
    final String name2 = ValueUtil.randomString();
    context.action( name, false, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction1 = context.getTransaction();
      assertEquals( transaction1.getName(), name );
      assertEquals( transaction1.getPrevious(), null );
      assertEquals( transaction1.getContext(), context );
      assertEquals( transaction1.getId(), nextNodeId );
      assertEquals( transaction1.isRootTransaction(), true );
      assertEquals( transaction1.getRootTransaction(), transaction1 );

      context.action( name2, false, () -> {
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
    final ArezContext context = new ArezContext();

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
  public void reportObserverError_when_spyEventHandler_PResent()
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
  public void getSpy_whenSpiesDisabled()
    throws Exception
  {
    ArezTestUtil.disableSpies();

    final ArezContext context = new ArezContext();

    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getSpy );
    assertEquals( exception.getMessage(), "Attempting to get Spy but spies are not enabled." );
  }

  @Test
  public void scheduleReaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

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
    final ArezContext context = new ArezContext();

    final Observer observer = newReadOnlyObserver( context );

    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.action( false, () -> context.scheduleReaction( observer ) ) );
    assertEquals( exception.getMessage(),
                  "Observer named '" + observer.getName() + "' attempted to be scheduled " +
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

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.scheduleReaction( derivation ) );
    assertEquals( exception.getMessage(),
                  "Observer named '" + derivation.getName() + "' attempted to schedule itself " +
                  "during read-only tracking transaction. Observers that are supporting ComputedValue " +
                  "instances must not schedule self." );
  }

  @Test
  public void scheduleReaction_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

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
  public void createComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> "";
    final Procedure onActivate = ValueUtil::randomString;
    final Procedure onDeactivate = ValueUtil::randomString;
    final Procedure onStale = ValueUtil::randomString;
    final Procedure onDispose = ValueUtil::randomString;
    final ComputedValue<String> computedValue =
      context.createComputedValue( name, function, Objects::equals, onActivate, onDeactivate, onStale, onDispose );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObservable().getName(), name );
    assertEquals( computedValue.getObserver().getOnActivate(), onActivate );
    assertEquals( computedValue.getObserver().getOnDeactivate(), onDeactivate );
    assertEquals( computedValue.getObserver().getOnStale(), onStale );
    assertEquals( computedValue.getObserver().getOnDispose(), onDispose );
  }

  @Test
  public void createComputedValue_withComponent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Component component =
      context.createComponent( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final String name = ValueUtil.randomString();
    final ComputedValue<String> computedValue =
      context.createComputedValue( component, name, () -> "", Objects::equals, null, null, null, null );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getComponent(), component );
  }

  @Test
  public void createComputedValue_pass_no_hooks()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> "";
    final ComputedValue<String> computedValue = context.createComputedValue( name, function, Objects::equals );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObservable().getName(), name );
    assertEquals( computedValue.getObserver().getOnActivate(), null );
    assertEquals( computedValue.getObserver().getOnDeactivate(), null );
    assertEquals( computedValue.getObserver().getOnStale(), null );
  }

  @Test
  public void createComputedValue_minimumParameters()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    context.setNextNodeId( 22 );
    final SafeFunction<String> function = () -> "";
    final ComputedValue<String> computedValue = context.createComputedValue( function );

    final String name = "ComputedValue@22";
    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObservable().getName(), name );
    assertEquals( computedValue.getObserver().getOnActivate(), null );
    assertEquals( computedValue.getObserver().getOnDeactivate(), null );
    assertEquals( computedValue.getObserver().getOnStale(), null );
  }

  @Test
  public void createComputedValue_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final ComputedValue<String> computedValue =
      context.createComputedValue( ValueUtil.randomString(), () -> "", Objects::equals );

    handler.assertEventCount( 1 );

    final ComputedValueCreatedEvent event = handler.assertEvent( ComputedValueCreatedEvent.class, 0 );
    assertEquals( event.getComputedValue().getName(), computedValue.getName() );
  }

  @Test
  public void autorun_minimumParameters()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    context.setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.autorun( callCount::incrementAndGet );

    assertEquals( observer.getName(), "Observer@22" );
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void autorun_withComponent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Component component =
      context.createComponent( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final AtomicInteger callCount = new AtomicInteger();
    final String name = ValueUtil.randomString();
    final Observer observer = context.autorun( component, name, true, callCount::incrementAndGet, false );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getComponent(), component );
  }

  @Test
  public void autorun_minimumParametersForMutation()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    context.setNextNodeId( 22 );
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.autorun( true, callCount::incrementAndGet );

    assertEquals( observer.getName(), "Observer@22" );
    assertEquals( observer.getMode(), TransactionMode.READ_WRITE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( callCount.get(), 1 );
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void autorun_runImmediately()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.autorun( name, true, callCount::incrementAndGet, true );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_WRITE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
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
  public void autorun_notRunImmediately()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.autorun( name, false, callCount::incrementAndGet, false );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
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
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.tracker( name, false, callCount::incrementAndGet );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );

    handler.assertEventCount( 1 );

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver().getName(), observer.getName() );
  }

  @Test
  public void tracker_withComponent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Component component =
      context.createComponent( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );
    final Observer observer = context.tracker( component, name, false, callCount::incrementAndGet );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getComponent(), component );
  }

  @Test
  public void tracker_minimalParameters()
    throws Exception
  {
    final ArezContext context = new ArezContext();

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
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final Observer observer =
      context.createObserver( null, ValueUtil.randomString(), true, new TestReaction(), false );

    handler.assertEventCount( 1 );

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver().getName(), observer.getName() );
  }

  @Test
  public void createObserver_canTrackExplicitly()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observer observer =
      context.createObserver( null, ValueUtil.randomString(), false, new TestReaction(), true );

    assertEquals( observer.canTrackExplicitly(), true );
  }

  @Test
  public void createObservable_no_parameters()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    context.setNextNodeId( 22 );
    final Observable<?> observable = context.createObservable();

    assertNotNull( observable.getName() );
    assertEquals( observable.getName(), "Observable@22" );
    assertNull( observable.getAccessor() );
    assertNull( observable.getMutator() );
  }

  @Test
  public void createObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final Observable<?> observable = context.createObservable( name );

    assertEquals( observable.getName(), name );
    assertEquals( observable.getAccessor(), null );
    assertEquals( observable.getMutator(), null );
  }

  @Test
  public void createObservable_withIntrospectors()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final PropertyAccessor<String> accessor = () -> "";
    final PropertyMutator<String> mutator = v -> {
    };
    final Observable<?> observable = context.createObservable( name, accessor, mutator );

    assertEquals( observable.getName(), name );
    assertEquals( observable.getAccessor(), accessor );
    assertEquals( observable.getMutator(), mutator );
  }

  @Test
  public void createObservable_withComponent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();

    final Component component =
      context.createComponent( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final Observable<String> observable = context.createObservable( component, name, null, null );

    assertEquals( observable.getName(), name );
    assertEquals( observable.getComponent(), component );
  }

  @Test
  public void createObservable_spyEventHandlerPresent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final Observable<?> observable = context.createObservable( name );

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

    final ArezContext context = new ArezContext();

    final Observable<?> observable = context.createObservable( null );

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
    context.autorun( ValueUtil.randomString(), false, callCount::incrementAndGet, false );
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
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Arez.context().releaseSchedulerLock() );

    assertEquals( exception.getMessage(), "releaseSchedulerLock() reduced schedulerLockCount below 0." );
  }

  @Test
  public void createComponent()
  {
    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();
    final String name = ValueUtil.randomString();

    assertFalse( context.isComponentPresent( type, id ) );

    final Component component = context.createComponent( type, id, name );

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
    final Component component = context.createComponent( type, id, name, preDispose, postDispose );

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

    final Component component = context.createComponent( type, id );

    assertTrue( context.isComponentPresent( type, id ) );

    assertEquals( component.getType(), type );
    assertEquals( component.getId(), id );
    assertEquals( component.getName(), type + "@" + id );
  }

  @Test
  public void createComponent_spyEventHandlerPresent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final Component component =
      context.createComponent( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

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

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.createComponent( type, id, name ) );

    assertEquals( exception.getMessage(),
                  "ArezContext.createComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void createComponent_duplicateComponent()
  {
    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();

    context.createComponent( type, id, ValueUtil.randomString() );

    assertTrue( context.isComponentPresent( type, id ) );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.createComponent( type, id, ValueUtil.randomString() ) );

    assertEquals( exception.getMessage(),
                  "ArezContext.createComponent() invoked for type '" + type + "' and id '" + id +
                  "' but a component already exists for specified type+id." );
  }

  @Test
  public void isComponentPresent_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.isComponentPresent( type, id ) );

    assertEquals( exception.getMessage(),
                  "ArezContext.isComponentPresent() invoked when Arez.areNativeComponentsEnabled() returns false." );
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

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.deregisterComponent( component ) );

    assertEquals( exception.getMessage(),
                  "ArezContext.deregisterComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
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
      context.createComponent( component.getType(), component.getId(), ValueUtil.randomString() );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.deregisterComponent( component ) );

    assertEquals( exception.getMessage(),
                  "ArezContext.deregisterComponent() invoked for '" + component + "' but was unable to " +
                  "remove specified component from registry. Actual component removed: " + component2 );
  }

  @Test
  public void deregisterComponent_removesTypeIfLastOfType()
  {
    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();
    final Component component =
      context.createComponent( type, ValueUtil.randomString(), ValueUtil.randomString() );
    final Component component2 =
      context.createComponent( type, ValueUtil.randomString(), ValueUtil.randomString() );

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

    final Component component = context.createComponent( type, id1, ValueUtil.randomString() );

    assertEquals( context.findAllComponentTypes().size(), 1 );
    assertEquals( context.findAllComponentTypes().contains( type ), true );

    assertEquals( context.findAllComponentsByType( ValueUtil.randomString() ).size(), 0 );

    assertEquals( context.findAllComponentsByType( type ).size(), 1 );
    assertEquals( context.findAllComponentsByType( type ).contains( component ), true );

    final Component component2 = context.createComponent( type, id2, ValueUtil.randomString() );

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

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.findComponent( type, id ) );

    assertEquals( exception.getMessage(),
                  "ArezContext.findComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllComponentsByType_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final String type = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.findAllComponentsByType( type ) );

    assertEquals( exception.getMessage(),
                  "ArezContext.findAllComponentsByType() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllComponentTypes_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, context::findAllComponentTypes );

    assertEquals( exception.getMessage(),
                  "ArezContext.findAllComponentTypes() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void registryAccessWhenDisabled()
  {
    ArezTestUtil.disableRegistries();

    final ArezContext context = Arez.context();

    final Observable<Object> observable = context.createObservable();
    final ComputedValue<String> computedValue = context.createComputedValue( () -> "" );
    final Observer observer = context.autorun( () -> {
    } );

    assertThrowsWithMessage( () -> context.registerObservable( observable ),
                             "ArezContext.registerObservable invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( () -> context.deregisterObservable( observable ),
                             "ArezContext.deregisterObservable invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( context::getTopLevelObservables,
                             "ArezContext.getTopLevelObservables() invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( () -> context.registerObserver( observer ),
                             "ArezContext.registerObserver invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( () -> context.deregisterObserver( observer ),
                             "ArezContext.deregisterObserver invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( context::getTopLevelObservers,
                             "ArezContext.getTopLevelObservers() invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( () -> context.registerComputedValue( computedValue ),
                             "ArezContext.registerComputedValue invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( () -> context.deregisterComputedValue( computedValue ),
                             "ArezContext.deregisterComputedValue invoked when Arez.areRegistriesEnabled() returns false." );
    assertThrowsWithMessage( context::getTopLevelComputedValues,
                             "ArezContext.getTopLevelComputedValues() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void observableRegistry()
  {
    final ArezContext context = Arez.context();

    final Observable<Object> observable = context.createObservable();

    assertEquals( context.getTopLevelObservables().size(), 1 );
    assertEquals( context.getTopLevelObservables().get( observable.getName() ), observable );

    assertThrowsWithMessage( () -> context.registerObservable( observable ),
                             "ArezContext.registerObservable invoked with observable named '" +
                             observable.getName() + "' but an existing observable with that name is " +
                             "already registered." );

    assertEquals( context.getTopLevelObservables().size(), 1 );
    context.getTopLevelObservables().clear();
    assertEquals( context.getTopLevelObservables().size(), 0 );

    assertThrowsWithMessage( () -> context.deregisterObservable( observable ),
                             "ArezContext.deregisterObservable invoked with observable named '" +
                             observable.getName() + "' but no observable with that name is registered." );
  }

  @Test
  public void observerRegistry()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.autorun( () -> {
    } );

    assertEquals( context.getTopLevelObservers().size(), 1 );
    assertEquals( context.getTopLevelObservers().get( observer.getName() ), observer );

    assertThrowsWithMessage( () -> context.registerObserver( observer ),
                             "ArezContext.registerObserver invoked with observer named '" +
                             observer.getName() + "' but an existing observer with that name is " +
                             "already registered." );

    assertEquals( context.getTopLevelObservers().size(), 1 );
    context.getTopLevelObservers().clear();
    assertEquals( context.getTopLevelObservers().size(), 0 );

    assertThrowsWithMessage( () -> context.deregisterObserver( observer ),
                             "ArezContext.deregisterObserver invoked with observer named '" +
                             observer.getName() + "' but no observer with that name is registered." );
  }

  @Test
  public void computedValueRegistry()
  {
    final ArezContext context = Arez.context();

    final ComputedValue computedValue = context.createComputedValue( () -> "" );

    assertEquals( context.getTopLevelComputedValues().size(), 1 );
    assertEquals( context.getTopLevelComputedValues().get( computedValue.getName() ), computedValue );

    assertThrowsWithMessage( () -> context.registerComputedValue( computedValue ),
                             "ArezContext.registerComputedValue invoked with computed value named '" +
                             computedValue.getName() + "' but an existing computed value with that name is " +
                             "already registered." );

    assertEquals( context.getTopLevelComputedValues().size(), 1 );
    context.getTopLevelComputedValues().clear();
    assertEquals( context.getTopLevelComputedValues().size(), 0 );

    assertThrowsWithMessage( () -> context.deregisterComputedValue( computedValue ),
                             "ArezContext.deregisterComputedValue invoked with computed value named '" +
                             computedValue.getName() + "' but no computed value with that name is registered." );
  }

  @Test
  public void when()
    throws Throwable
  {
    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      return false;
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    final Disposable node = Arez.context().when( name, true, condition, procedure );

    assertTrue( node instanceof Watcher );
    final Watcher watcher = (Watcher) node;
    assertEquals( watcher.getName(), name );
    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }

  @Test
  public void when_minimalParameters()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Observable observable = context.createObservable();

    final AtomicBoolean result = new AtomicBoolean();

    final AtomicInteger conditionRun = new AtomicInteger();
    final AtomicInteger effectRun = new AtomicInteger();

    final SafeFunction<Boolean> condition = () -> {
      conditionRun.incrementAndGet();
      observable.reportObserved();
      return result.get();
    };
    final SafeProcedure procedure = effectRun::incrementAndGet;

    final Disposable node = context.when( condition, procedure );

    assertTrue( node instanceof Watcher );
    final Watcher watcher = (Watcher) node;
    assertEquals( watcher.getName(), "When@2", "The name has @2 as one other Arez entity created (Observable)" );
    assertEquals( conditionRun.get(), 1 );
    assertEquals( effectRun.get(), 0 );
  }

  @Test
  public void noTxAction_function()
    throws Throwable
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();
    final String v0 =
      context.action( () -> {
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
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();
    final String v0 =
      context.action( () -> {
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
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    context.action( () -> {
      assertTrue( context.isTransactionActive() );

      context.noTxAction( () -> {
        assertFalse( context.isTransactionActive() );
      } );

      assertTrue( context.isTransactionActive() );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void noTxAction_safeProcedure()
    throws Throwable
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    context.action( () -> {
      assertTrue( context.isTransactionActive() );

      context.safeNoTxAction( () -> {
        assertFalse( context.isTransactionActive() );
      } );

      assertTrue( context.isTransactionActive() );
    } );

    assertFalse( context.isTransactionActive() );
  }

  private void assertThrowsWithMessage( @Nonnull final ThrowingRunnable runnable, @Nonnull final String message )
  {
    assertEquals( expectThrows( IllegalStateException.class, runnable ).getMessage(), message );
  }
}
