package org.realityforge.arez.test;

import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.arez.AbstractArezTest;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Observer;
import org.realityforge.arez.ObserverErrorHandler;
import org.realityforge.arez.ObserverState;
import org.realityforge.arez.Reaction;
import org.realityforge.arez.TransactionMode;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * This class tests all the public methods of ArezContext
 * that should be visible outside package.
 */
public class ArezContextApiTest
  extends AbstractArezTest
{
  @Test
  public void createObserver_notReaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final String name = ValueUtil.randomString();
    final Observer observer =
      context.createObserver( name, TransactionMode.READ_ONLY, null, false );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void createReactionObserver()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final AtomicInteger callCount = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final Reaction reaction = o -> callCount.incrementAndGet();
    final Observer observer =
      context.createObserver( name, TransactionMode.READ_ONLY, reaction, true );

    assertEquals( observer.getName(), name );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
    assertEquals( callCount.get(), 1 );

    observer.dispose();

    assertEquals( observer.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void observerErrorHandler()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final AtomicInteger callCount = new AtomicInteger();

    final ObserverErrorHandler handler = ( observer, error, throwable ) -> callCount.incrementAndGet();
    context.addObserverErrorHandler( handler );

    final Reaction reaction = o -> {
      throw new RuntimeException();
    };
    // This will run immediately and generate an exception
    context.createObserver( ValueUtil.randomString(), TransactionMode.READ_ONLY, reaction, true );

    assertEquals( callCount.get(), 1 );

    context.removeObserverErrorHandler( handler );

    // This will run immediately and generate an exception
    context.createObserver( ValueUtil.randomString(), TransactionMode.READ_ONLY, reaction, true );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void transactionsCanProduceValues()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.transaction( ValueUtil.randomString(), TransactionMode.READ_ONLY, null, () -> {
        assertTrue( context.isTransactionActive() );
        return expectedValue;
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );
  }

  @Test
  public void transactionsCanBeNested()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    context.transaction( ValueUtil.randomString(), TransactionMode.READ_ONLY, null, () -> {
      assertTrue( context.isTransactionActive() );

      //First nested exception
      context.transaction( ValueUtil.randomString(), TransactionMode.READ_ONLY, null, () -> {
        assertTrue( context.isTransactionActive() );

        //Second nested exception
        context.transaction( ValueUtil.randomString(),
                             TransactionMode.READ_ONLY,
                             null,
                             () -> assertTrue( context.isTransactionActive() ) );

        assertTrue( context.isTransactionActive() );
      } );

      assertTrue( context.isTransactionActive() );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void valueProducingTransactionsCanBeNested()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );

    final String expectedValue = ValueUtil.randomString();

    final String v0 =
      context.transaction( ValueUtil.randomString(), TransactionMode.READ_ONLY, null, () -> {
        assertTrue( context.isTransactionActive() );

        //First nested exception
        final String v1 =
          context.transaction( ValueUtil.randomString(), TransactionMode.READ_ONLY, null, () -> {
            assertTrue( context.isTransactionActive() );

            //Second nested exception
            final String v2 =
              context.transaction( ValueUtil.randomString(), TransactionMode.READ_ONLY, null, () -> {
                assertTrue( context.isTransactionActive() );
                return expectedValue;
              } );

            assertTrue( context.isTransactionActive() );

            return v2;
          } );

        assertTrue( context.isTransactionActive() );
        return v1;
      } );

    assertFalse( context.isTransactionActive() );

    assertEquals( v0, expectedValue );
  }
}
