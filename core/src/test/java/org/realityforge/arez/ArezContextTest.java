package org.realityforge.arez;

import java.io.IOException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;
import org.realityforge.arez.spy.ComputedValueCreatedEvent;
import org.realityforge.arez.spy.ObservableCreatedEvent;
import org.realityforge.arez.spy.ObserverCreatedEvent;
import org.realityforge.arez.spy.ObserverErrorEvent;
import org.realityforge.arez.spy.ReactionCompletedEvent;
import org.realityforge.arez.spy.ReactionScheduledEvent;
import org.realityforge.arez.spy.ReactionStartedEvent;
import org.realityforge.arez.spy.TransactionCompletedEvent;
import org.realityforge.arez.spy.TransactionStartedEvent;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class ArezContextTest
  extends AbstractArezTest
{
  @Test
  public void areNamesEnabled()
  {
    final ArezContext context = new ArezContext();
    assertTrue( context.areNamesEnabled() );
  }

  @Test
  public void areSpiesEnabled()
  {
    final ArezContext context = new ArezContext();
    assertTrue( context.areSpiesEnabled() );
  }

  @Test
  public void toName()
  {
    final ArezContext context = new ArezContext();

    // Use passed in name
    assertEquals( context.toName( "ComputedValue", "MyName" ), "MyName" );

    //synthesize name
    context.setNextNodeId( 1 );
    assertEquals( context.toName( "ComputedValue", null ), "ComputedValue@1" );
    assertEquals( context.getNextNodeId(), 2 );

    getConfigProvider().setEnableNames( false );

    //Ignore name
    assertEquals( context.toName( "ComputedValue", "MyName" ), null );

    //Null name also fine
    assertEquals( context.toName( "ComputedValue", null ), null );
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
  public void beginTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final String name = ValueUtil.randomString();
    final TransactionMode mode = TransactionMode.READ_ONLY;
    final Observer tracker = null;
    final Transaction transaction = context.beginTransaction( name, mode, tracker );

    assertTrue( context.isTransactionActive() );

    assertEquals( context.getTransaction(), transaction );
    assertEquals( transaction.getContext(), context );
    assertEquals( transaction.getName(), name );
    assertEquals( transaction.getMode(), mode );
    assertEquals( transaction.getTracker(), tracker );
    assertEquals( transaction.getPrevious(), null );
    assertEquals( transaction.getMode(), TransactionMode.READ_ONLY );
  }

  @Test
  public void beginTransaction_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final Observer tracker = null;
    context.beginTransaction( name, TransactionMode.READ_ONLY, tracker );

    handler.assertEventCount( 1 );
    final TransactionStartedEvent event = handler.assertEvent( TransactionStartedEvent.class, 0 );
    assertEquals( event.getName(), name );
    assertEquals( event.isMutation(), false );
    assertEquals( event.getTracker(), tracker );
  }

  @Test
  public void beginTransaction_attemptToNest_READ_WRITE_in_READ_ONLY()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    setCurrentTransaction( newReadOnlyObserver( context ) );
    final Transaction transaction = context.getTransaction();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.beginTransaction( name, TransactionMode.READ_WRITE, null ) );
    assertEquals( exception.getMessage(),
                  "Attempting to create READ_WRITE transaction named '" + name +
                  "' but it is nested in transaction named '" + transaction.getName() + "' with " +
                  "mode READ_ONLY which is not equal to READ_WRITE." );
  }

  @Test
  public void beginTransaction_attemptToNest_READ_WRITE_in_READ_WRITE_OWNED()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    setCurrentTransaction( newDerivation( context ) );
    final Transaction transaction = context.getTransaction();

    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.beginTransaction( name, TransactionMode.READ_WRITE, null ) );
    assertEquals( exception.getMessage(),
                  "Attempting to create READ_WRITE transaction named '" + name +
                  "' but it is nested in transaction named '" + transaction.getName() + "' with " +
                  "mode READ_WRITE_OWNED which is not equal to READ_WRITE." );
  }

  @Test
  public void commitTransaction_matchingRootTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    context.setTransaction( transaction );

    context.commitTransaction( transaction );

    assertEquals( context.isTransactionActive(), false );
  }

  @SuppressWarnings( "ConstantConditions" )
  @Test
  public void commitTransaction_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final Observer tracker = null;
    final Transaction transaction =
      new Transaction( context, null, name, TransactionMode.READ_ONLY, tracker );
    transaction.begin();
    context.setTransaction( transaction );

    context.commitTransaction( transaction );

    handler.assertEventCount( 1 );
    final TransactionCompletedEvent event = handler.assertEvent( TransactionCompletedEvent.class, 0 );
    assertEquals( event.getName(), name );
    assertEquals( event.isMutation(), false );
    assertEquals( event.getTracker(), tracker );
    assertTrue( event.getDuration() >= 0 );
  }

  @Test
  public void commitTransaction_nonMatchingRootTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();
    context.setTransaction( transaction );

    final Transaction transaction2 =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction2.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.commitTransaction( transaction2 ) );
    assertEquals( exception.getMessage(),
                  "Attempting to commit transaction named '" + transaction2.getName() +
                  "' but this does not match existing transaction named '" + transaction.getName() + "'." );
  }

  @Test
  public void commitTransaction_noTransactionActive()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Transaction transaction =
      new Transaction( context, null, ValueUtil.randomString(), TransactionMode.READ_ONLY, null );
    transaction.begin();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.commitTransaction( transaction ) );
    assertEquals( exception.getMessage(),
                  "Attempting to commit transaction named '" + transaction.getName() +
                  "' but no transaction is active." );
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

    final Observable observable = newObservable( context );
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

    final Observable observable = newObservable( context );
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
        final ArrayList<Observable> observables = transaction.getObservables();
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
      assertEquals( e.getTracker(), tracker );
    }
    {
      final TransactionCompletedEvent e = handler.assertEvent( TransactionCompletedEvent.class, 2 );
      assertEquals( e.isMutation(), true );
      assertEquals( e.getTracker(), tracker );
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

    final Observable observable = newObservable( context );
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
  public void track_safeFunction()
    throws Throwable
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();

    final AtomicInteger callCount = new AtomicInteger();

    final Observer tracker = context.tracker( callCount::incrementAndGet );

    final Observable observable = newObservable( context );
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
        final ArrayList<Observable> observables = transaction.getObservables();
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

    final Observable observable = newObservable( context );
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
      final ArrayList<Observable> observables = transaction.getObservables();
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

    final Observable observable = newObservable( context );
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
      final ArrayList<Observable> observables = transaction.getObservables();
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

    final Observable observable = newObservable( context );
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

    final Observable observable = newObservable( context );
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

    assertEquals( event.getObserver(), observer );
    assertEquals( event.getError(), observerError );
    assertEquals( event.getThrowable(), throwable );
  }

  @Test
  public void getSpy_whenSpiesDisabled()
    throws Exception
  {
    getConfigProvider().setEnableSpy( false );

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
    assertEquals( event.getObserver(), observer );
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
    assertEquals( event.getComputedValue(), computedValue );
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

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver(), observer );
    assertEquals( handler.assertEvent( ReactionStartedEvent.class, 1 ).getObserver(), observer );
    assertEquals( handler.assertEvent( ActionStartedEvent.class, 2 ).getName(), observer.getName() );
    assertEquals( handler.assertEvent( TransactionStartedEvent.class, 3 ).getTracker(), observer );
    assertEquals( handler.assertEvent( TransactionCompletedEvent.class, 4 ).getTracker(), observer );
    assertEquals( handler.assertEvent( ActionCompletedEvent.class, 5 ).getName(), observer.getName() );
    assertEquals( handler.assertEvent( ReactionCompletedEvent.class, 6 ).getObserver(), observer );
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

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver(), observer );
    assertEquals( handler.assertEvent( ReactionScheduledEvent.class, 1 ).getObserver(), observer );
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

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver(), observer );
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

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver(), observer );
  }

  @Test
  public void createObserver_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final Observer observer =
      context.createObserver( ValueUtil.randomString(), true, new TestReaction(), false );

    handler.assertEventCount( 1 );

    assertEquals( handler.assertEvent( ObserverCreatedEvent.class, 0 ).getObserver(), observer );
  }

  @Test
  public void createObserver_canTrackExplicitly()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observer observer =
      context.createObserver( ValueUtil.randomString(), false, new TestReaction(), true );

    assertEquals( observer.canTrackExplicitly(), true );
  }

  @Test
  public void createObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final Observable observable = context.createObservable( name );

    assertEquals( observable.getName(), name );
  }

  @Test
  public void createObservable_spyEventHandlerPresent()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final Observable observable = context.createObservable( name );

    assertEquals( observable.getName(), name );
    handler.assertEventCount( 1 );
    final ObservableCreatedEvent event = handler.assertEvent( ObservableCreatedEvent.class, 0 );
    assertEquals( event.getObservable(), observable );
  }

  @Test
  public void createObservable_name_Null()
    throws Exception
  {
    getConfigProvider().setEnableNames( false );

    final ArezContext context = new ArezContext();

    final Observable observable = context.createObservable( null );

    assertNotNull( observable );
  }
}
