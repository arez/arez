package org.realityforge.arez.api2;

import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class TransactionTest
  extends AbstractArezTest
{
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
