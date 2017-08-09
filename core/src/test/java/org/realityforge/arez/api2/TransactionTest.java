package org.realityforge.arez.api2;

import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TransactionTest
  extends AbstractArezTest
{
  @Test
  public void singleNestedTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    assertThrows( context::getTransaction );

    final int nextNodeId = context.currentNextNodeId();
    final String name = ValueUtil.randomString();
    context.transaction( name, null, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction = context.getTransaction();
      assertEquals( transaction.getName(), name );
      assertEquals( transaction.getPrevious(), null );
      assertEquals( transaction.getContext(), context );
      assertEquals( transaction.getId(), nextNodeId );
    } );

    assertFalse( context.isTransactionActive() );
  }

  @Test
  public void doubleNestedTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    assertFalse( context.isTransactionActive() );
    assertThrows( context::getTransaction );

    final int nextNodeId = context.currentNextNodeId();
    final String name = ValueUtil.randomString();
    final String name2 = ValueUtil.randomString();
    context.transaction( name, null, () -> {
      assertTrue( context.isTransactionActive() );
      final Transaction transaction1 = context.getTransaction();
      assertEquals( transaction1.getName(), name );
      assertEquals( transaction1.getPrevious(), null );
      assertEquals( transaction1.getContext(), context );
      assertEquals( transaction1.getId(), nextNodeId );
      assertEquals( transaction1.isRootTransaction(), true );
      assertEquals( transaction1.getRootTransaction(), transaction1 );

      context.transaction( name2, null, () -> {
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
  public void transactionAcceptsNullNameWhen_namesDisabled()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = (ArezConfig.DynamicProvider) ArezConfig.getProvider();
    provider.setEnableNames( false );

    final ArezContext context = new ArezContext();

    context.transaction( null, null, () -> assertThrows( () -> context.getTransaction().getName() ) );
  }

  @Test
  public void transactionAcceptsNoNameWhen_namesDisabled()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = (ArezConfig.DynamicProvider) ArezConfig.getProvider();
    provider.setEnableNames( false );

    final ArezContext context = new ArezContext();

    assertThrows( () -> context.transaction( ValueUtil.randomString(), null, () -> {
      assertEquals( context.getTransaction().getName(), null );
    } ) );
  }
}
