package org.realityforge.arez.test;

import org.realityforge.arez.AbstractArezTest;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.TransactionMode;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TransactionApiTest
  extends AbstractArezTest
{
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
        context.transaction( ValueUtil.randomString(), TransactionMode.READ_ONLY, null, () -> {
          assertTrue( context.isTransactionActive() );
        } );

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
