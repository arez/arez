package org.realityforge.arez;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class ArezContextTest
  extends AbstractArezTest
{
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
  public void function()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

    final String expectedValue = ValueUtil.randomString();

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();

    final String v0 =
      context.function( name, false, () -> {
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
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    //Observable still not updated
    assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );
    assertEquals( observable.getObservers().size(), 0 );
  }

  @Test
  public void safeFunction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

    final String expectedValue = ValueUtil.randomString();

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();

    final String v0 =
      context.safeFunction( name, false, () -> {
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
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );

    //Observable still not updated
    assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );
    assertEquals( observable.getObservers().size(), 0 );
  }

  @Test
  public void nonTrackingProcedureObservingSingleObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    final IllegalStateException exception = expectThrows( IllegalStateException.class, context::getTransaction );
    assertEquals( exception.getMessage(), "Attempting to get current transaction but no transaction is active." );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    assertEquals( observable.getObservers().size(), 0 );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();
    context.procedure( name, false, () -> {
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
    } );

    assertFalse( context.isTransactionActive() );

    //Observable still not updated
    assertNotEquals( nextNodeId, observable.getLastTrackerTransactionId() );
    assertEquals( observable.getObservers().size(), 0 );
  }

  @Test
  public void nestedProceduresAccessingSameObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    assertThrows( context::getTransaction );

    final int nextNodeId = context.currentNextTransactionId();
    final String name = ValueUtil.randomString();
    final String name2 = ValueUtil.randomString();
    context.procedure( name, false, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction1 = context.getTransaction();
      assertEquals( transaction1.getName(), name );
      assertEquals( transaction1.getPrevious(), null );
      assertEquals( transaction1.getContext(), context );
      assertEquals( transaction1.getId(), nextNodeId );
      assertEquals( transaction1.isRootTransaction(), true );
      assertEquals( transaction1.getRootTransaction(), transaction1 );

      context.procedure( name2, false, () -> {
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

    final ObserverErrorHandler handler = ( observer, error, throwable ) -> {
    };

    context.addObserverErrorHandler( handler );

    assertEquals( context.getObserverErrorHandlerSupport().getObserverErrorHandlers().size(), 1 );
    assertEquals( context.getObserverErrorHandlerSupport().getObserverErrorHandlers().contains( handler ), true );

    context.removeObserverErrorHandler( handler );

    assertEquals( context.getObserverErrorHandlerSupport().getObserverErrorHandlers().size(), 0 );
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
  public void createComputedValue()
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
  }

  @Test
  public void autorun_runImmediately()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer =
      context.autorun( name, true, callCount::incrementAndGet, true );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_WRITE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.hasReaction(), true );
    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void autorun_notRunImmediately()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final AtomicInteger callCount = new AtomicInteger();
    final Observer observer = context.autorun( name, false, callCount::incrementAndGet, false );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.hasReaction(), true );
    assertEquals( callCount.get(), 0 );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
  }

  @Test
  public void createObserver_runImmediately()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final TestReaction reaction = new TestReaction();
    final Observer observer =
      context.createObserver( name, true, reaction, true );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_WRITE );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( observer.getReaction(), reaction );
    assertEquals( reaction.getCallCount(), 1 );
  }

  @Test
  public void createObserver_notRunImmediately()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final TestReaction reaction = new TestReaction();
    final Observer observer = context.createObserver( name, false, reaction, false );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.getReaction(), reaction );
    assertEquals( reaction.getCallCount(), 0 );
    assertEquals( context.getScheduler().getPendingObservers().size(), 1 );
  }

  @Test
  public void createObserver_notAReaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final Observer observer = context.createObserver( name, false, null, false );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getMode(), TransactionMode.READ_ONLY );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
    assertEquals( observer.getReaction(), null );
    assertEquals( context.getScheduler().getPendingObservers().size(), 0 );
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
  public void createObservable_name_Null()
    throws Exception
  {
    getConfigProvider().setEnableNames( false );

    final ArezContext context = new ArezContext();

    final Observable observable = context.createObservable( null );

    assertNotNull( observable );
  }
}
