package org.realityforge.arez.api2;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class GuardsTest
  extends AbstractArezTest
{
  @Test
  public void fail()
    throws Exception
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.fail( () -> "My Failure Reason" ) );

    assertEquals( exception.getMessage(), "My Failure Reason" );
  }

  @Test
  public void fail_verboseErrorMessages_false()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = (ArezConfig.DynamicProvider) ArezConfig.getProvider();
    provider.setVerboseErrorMessages( false );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.fail( () -> "My Failure Reason" ) );

    assertEquals( exception.getMessage(), null );
  }

  @Test
  public void fail_checkInvariants_false()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = (ArezConfig.DynamicProvider) ArezConfig.getProvider();
    provider.setCheckInvariants( false );

    // No failure
    Guards.fail( () -> "My Failure Reason" );
  }

  @Test
  public void invariant_passed()
    throws Exception
  {
    Guards.invariant( () -> true, () -> "My Failure Reason" );
  }

  @Test
  public void invariant_failed()
    throws Exception
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.invariant( () -> false, () -> "My Failure Reason" ) );

    assertEquals( exception.getMessage(), "My Failure Reason" );
  }

  @Test
  public void invariant_condition_throws_exception()
    throws Exception
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.invariant( () -> {
        throw new RuntimeException( "X" );
      }, () -> "My Failure Reason" ) );

    final String message = exception.getMessage();
    assertTrue( message.startsWith( "Error checking condition.\n" +
                                    "Message: My Failure Reason\n" +
                                    "Throwable:\n" +
                                    "java.lang.RuntimeException: X" ) );
  }

  @Test
  public void invariant_verboseErrorMessages_false()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = (ArezConfig.DynamicProvider) ArezConfig.getProvider();
    provider.setVerboseErrorMessages( false );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.invariant( () -> false, () -> "My Failure Reason" ) );

    assertEquals( exception.getMessage(), null );
  }

  @Test
  public void invariant_checkInvariants_false()
    throws Exception
  {
    final ArezConfig.DynamicProvider provider = (ArezConfig.DynamicProvider) ArezConfig.getProvider();
    provider.setCheckInvariants( false );

    // No failure
    Guards.invariant( () -> false, () -> "My Failure Reason" );
  }
}
