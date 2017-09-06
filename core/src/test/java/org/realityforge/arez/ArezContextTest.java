package org.realityforge.arez;

import java.util.ArrayList;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

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

    final int nextNodeId = context.currentNextNodeId();
    final String name = ValueUtil.randomString();

    final String v0 =
      context.function( name, false, null, () -> {
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

    final int nextNodeId = context.currentNextNodeId();
    final String name = ValueUtil.randomString();

    final String v0 =
      context.safeFunction( name, false, null, () -> {
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

    final int nextNodeId = context.currentNextNodeId();
    final String name = ValueUtil.randomString();
    context.procedure( name, false, null, () -> {
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
  public void trackingProcedureObservingSingleObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    assertThrows( context::getTransaction );

    final Observer tracker = new Observer( context, ValueUtil.randomString() );

    final ArrayList<Observable> dependencies = tracker.getDependencies();
    assertEquals( dependencies.size(), 0 );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    assertEquals( observable.getObservers().size(), 0 );

    assertEquals( tracker.getState(), ObserverState.INACTIVE );

    final int nextNodeId = context.currentNextNodeId();
    final String name = ValueUtil.randomString();
    context.procedure( name, false, tracker, () -> {

      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), name );
      assertEquals( transaction.getPrevious(), null );
      assertEquals( transaction.getContext(), context );
      assertEquals( transaction.getId(), nextNodeId );
      assertEquals( transaction.getTracker(), tracker );

      assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );

      // The dependencies reference is only updated on completion
      assertTrue( dependencies == tracker.getDependencies() );

      assertEquals( tracker.getDependencies().size(), 0 );
      assertEquals( observable.getObservers().size(), 0 );
      assertNotEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );

      observable.reportObserved();

      assertEquals( tracker.getDependencies().size(),
                    0,
                    "Ensure observers are added on completion and not immediately" );
      assertEquals( observable.getObservers().size(), 0 );
      assertEquals( context.getTransaction().getId(),
                    observable.getLastTrackerTransactionId(),
                    "LastTrackerTransactionId should be updated immediately" );

      // Another observation should not change state as already observed
      observable.reportObserved();

      assertEquals( tracker.getDependencies().size(), 0 );
      assertEquals( observable.getObservers().size(), 0 );
      assertEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );

      // The dependencies reference is only updated on completion
      assertTrue( dependencies == tracker.getDependencies() );
    } );

    final ArrayList<Observable> postDependencies = tracker.getDependencies();

    // The dependencies reference should be swapped out by now
    assertFalse( dependencies == postDependencies );

    // A single dependency that matches the observable
    assertEquals( postDependencies.size(), 1 );
    assertTrue( postDependencies.contains( observable ) );

    // Ensure that the observable has observers updated
    assertEquals( observable.getObservers().size(), 1 );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void safeProcedure_withSingleObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    assertThrows( context::getTransaction );

    final Observer tracker = new Observer( context, ValueUtil.randomString() );

    final ArrayList<Observable> dependencies = tracker.getDependencies();
    assertEquals( dependencies.size(), 0 );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    assertEquals( observable.getObservers().size(), 0 );

    assertEquals( tracker.getState(), ObserverState.INACTIVE );

    final int nextNodeId = context.currentNextNodeId();
    final String name = ValueUtil.randomString();
    context.safeProcedure( name, false, tracker, () -> {

      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), name );
      assertEquals( transaction.getPrevious(), null );
      assertEquals( transaction.getContext(), context );
      assertEquals( transaction.getId(), nextNodeId );
      assertEquals( transaction.getTracker(), tracker );

      assertEquals( tracker.getState(), ObserverState.UP_TO_DATE );

      // The dependencies reference is only updated on completion
      assertTrue( dependencies == tracker.getDependencies() );

      assertEquals( tracker.getDependencies().size(), 0 );
      assertEquals( observable.getObservers().size(), 0 );
      assertNotEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );

      observable.reportObserved();

      assertEquals( tracker.getDependencies().size(),
                    0,
                    "Ensure observers are added on completion and not immediately" );
      assertEquals( observable.getObservers().size(), 0 );
      assertEquals( context.getTransaction().getId(),
                    observable.getLastTrackerTransactionId(),
                    "LastTrackerTransactionId should be updated immediately" );

      // Another observation should not change state as already observed
      observable.reportObserved();

      assertEquals( tracker.getDependencies().size(), 0 );
      assertEquals( observable.getObservers().size(), 0 );
      assertEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );

      // The dependencies reference is only updated on completion
      assertTrue( dependencies == tracker.getDependencies() );
    } );

    final ArrayList<Observable> postDependencies = tracker.getDependencies();

    // The dependencies reference should be swapped out by now
    assertFalse( dependencies == postDependencies );

    // A single dependency that matches the observable
    assertEquals( postDependencies.size(), 1 );
    assertTrue( postDependencies.contains( observable ) );

    // Ensure that the observable has observers updated
    assertEquals( observable.getObservers().size(), 1 );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void nestedTrackingProceduresAccessingSameObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    assertThrows( context::getTransaction );

    final Observer tracker1 = new Observer( context, ValueUtil.randomString() );
    final Observer tracker2 = new Observer( context, ValueUtil.randomString() );
    final Observer tracker3 = new Observer( context, ValueUtil.randomString() );

    final ArrayList<Observable> dependencies1 = tracker1.getDependencies();
    final ArrayList<Observable> dependencies2 = tracker2.getDependencies();
    final ArrayList<Observable> dependencies3 = tracker3.getDependencies();

    assertEquals( dependencies1.size(), 0 );
    assertEquals( dependencies2.size(), 0 );
    assertEquals( dependencies3.size(), 0 );

    final Observable observable = new Observable( context, ValueUtil.randomString() );
    assertEquals( observable.getObservers().size(), 0 );

    assertEquals( tracker1.getState(), ObserverState.INACTIVE );

    final int nextNodeId = context.currentNextNodeId();
    final String name1 = ValueUtil.randomString();
    final String name2 = ValueUtil.randomString();
    final String name3 = ValueUtil.randomString();

    context.procedure( name1, false, tracker1, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction1 = context.getTransaction();
      assertEquals( transaction1.getName(), name1 );
      assertEquals( transaction1.getPrevious(), null );
      assertEquals( transaction1.getContext(), context );
      assertEquals( transaction1.getId(), nextNodeId );
      assertEquals( transaction1.isRootTransaction(), true );
      assertEquals( transaction1.getRootTransaction(), transaction1 );
      assertEquals( transaction1.getTracker(), tracker1 );
      assertEquals( transaction1.getMode(), TransactionMode.READ_ONLY );

      assertEquals( tracker1.getState(), ObserverState.UP_TO_DATE );
      // The dependencies reference is only updated on completion
      assertTrue( dependencies1 == tracker1.getDependencies() );

      observable.reportObserved();

      assertEquals( tracker1.getDependencies().size(), 0 );
      assertEquals( tracker2.getDependencies().size(), 0 );
      assertEquals( tracker3.getDependencies().size(), 0 );
      assertEquals( observable.getObservers().size(), 0 );
      assertEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );

      context.procedure( name2, false, tracker2, () -> {
        assertTrue( context.isTransactionActive() );
        final Transaction transaction2 = context.getTransaction();
        assertEquals( transaction2.getName(), name2 );
        assertEquals( transaction2.getPrevious(), transaction1 );
        assertEquals( transaction2.getContext(), context );
        assertEquals( transaction2.getId(), nextNodeId + 1 );
        assertEquals( transaction2.isRootTransaction(), false );
        assertEquals( transaction2.getRootTransaction(), transaction1 );
        assertEquals( transaction2.getTracker(), tracker2 );
        assertEquals( transaction2.getMode(), TransactionMode.READ_ONLY );

        assertEquals( tracker1.getDependencies().size(), 0 );
        assertEquals( tracker2.getDependencies().size(), 0 );
        assertEquals( tracker3.getDependencies().size(), 0 );
        assertEquals( observable.getObservers().size(), 0 );
        assertNotEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );

        observable.reportObserved();

        assertEquals( tracker1.getDependencies().size(), 0 );
        assertEquals( tracker2.getDependencies().size(), 0 );
        assertEquals( tracker3.getDependencies().size(), 0 );
        assertEquals( observable.getObservers().size(), 0 );
        assertEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );

        context.procedure( name3, false, tracker3, () -> {
          final Transaction transaction3 = context.getTransaction();
          assertEquals( transaction3.getName(), name3 );
          assertEquals( transaction3.getPrevious(), transaction2 );
          assertEquals( transaction3.getContext(), context );
          assertEquals( transaction3.getId(), nextNodeId + 2 );
          assertEquals( transaction3.isRootTransaction(), false );
          assertEquals( transaction3.getRootTransaction(), transaction1 );
          assertEquals( transaction3.getTracker(), tracker3 );
          assertEquals( transaction3.getMode(), TransactionMode.READ_ONLY );

          assertEquals( tracker1.getDependencies().size(), 0 );
          assertEquals( tracker2.getDependencies().size(), 0 );
          assertEquals( tracker3.getDependencies().size(), 0 );
          assertEquals( observable.getObservers().size(), 0 );
          assertNotEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );

          observable.reportObserved();

          assertEquals( tracker1.getDependencies().size(), 0 );
          assertEquals( tracker2.getDependencies().size(), 0 );
          assertEquals( tracker3.getDependencies().size(), 0 );
          assertEquals( observable.getObservers().size(), 0 );
          assertEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );
        } );

        assertEquals( context.getTransaction(), transaction2 );

        assertEquals( tracker1.getDependencies().size(), 0 );
        assertEquals( tracker2.getDependencies().size(), 0 );
        assertEquals( tracker3.getDependencies().size(), 1 );
        assertEquals( tracker3.getDependencies().contains( observable ), true );
        assertEquals( observable.getObservers().size(), 1 );
        assertEquals( observable.getObservers().contains( tracker3 ), true );
        assertNotEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );
      } );

      assertEquals( context.getTransaction(), transaction1 );

      assertEquals( tracker1.getDependencies().size(), 0 );
      assertEquals( tracker2.getDependencies().size(), 1 );
      assertEquals( tracker2.getDependencies().contains( observable ), true );
      assertEquals( tracker3.getDependencies().size(), 1 );
      assertEquals( tracker3.getDependencies().contains( observable ), true );
      assertEquals( observable.getObservers().size(), 2 );
      assertEquals( observable.getObservers().contains( tracker2 ), true );
      assertEquals( observable.getObservers().contains( tracker3 ), true );
      assertNotEquals( context.getTransaction().getId(), observable.getLastTrackerTransactionId() );

      // Another observation should not change state as already observed
      observable.reportObserved();

      assertEquals( observable.getObservers().size(), 2 );
      assertEquals( tracker1.getDependencies().size(), 0 );
    } );

    // All the trackers should now have dependency tracked

    assertEquals( tracker1.getDependencies().size(), 1 );
    assertEquals( tracker1.getDependencies().contains( observable ), true );
    assertEquals( tracker2.getDependencies().size(), 1 );
    assertEquals( tracker2.getDependencies().contains( observable ), true );
    assertEquals( tracker3.getDependencies().size(), 1 );
    assertEquals( tracker3.getDependencies().contains( observable ), true );
    assertEquals( observable.getObservers().size(), 3 );
    assertEquals( observable.getObservers().contains( tracker1 ), true );
    assertEquals( observable.getObservers().contains( tracker2 ), true );
    assertEquals( observable.getObservers().contains( tracker3 ), true );
  }

  @Test
  public void nestedNonTrackingProceduresAccessingSameObservable()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    assertThrows( context::getTransaction );

    final int nextNodeId = context.currentNextNodeId();
    final String name = ValueUtil.randomString();
    final String name2 = ValueUtil.randomString();
    context.procedure( name, false, null, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction1 = context.getTransaction();
      assertEquals( transaction1.getName(), name );
      assertEquals( transaction1.getPrevious(), null );
      assertEquals( transaction1.getContext(), context );
      assertEquals( transaction1.getId(), nextNodeId );
      assertEquals( transaction1.isRootTransaction(), true );
      assertEquals( transaction1.getRootTransaction(), transaction1 );

      context.procedure( name2, false, null, () -> {
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

    assertEquals( context.currentNextNodeId(), 1 );
    assertEquals( context.nextNodeId(), 1 );
    assertEquals( context.currentNextNodeId(), 2 );
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
