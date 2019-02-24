package arez;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class GuardsTest
  extends AbstractArezTest
{
  @Test
  public void fail()
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.fail( () -> "My Failure Reason" ) );

    assertEquals( exception.getMessage(), "My Failure Reason" );
  }

  @Test
  public void fail_noCheckInvariants()
  {
    ArezTestUtil.noCheckInvariants();

    // No failure
    Guards.fail( () -> "My Failure Reason" );
  }

  @Test
  public void fail_noCheckApiInvariants()
  {
    ArezTestUtil.noCheckApiInvariants();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.fail( () -> "My Failure Reason" ) );

    assertEquals( exception.getMessage(), "My Failure Reason" );
  }

  @Test
  public void invariant_passed()
  {
    Guards.invariant( () -> true, () -> "My Failure Reason" );
  }

  @Test
  public void invariant_failed()
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.invariant( () -> false, () -> "My Failure Reason" ) );

    assertEquals( exception.getMessage(), "My Failure Reason" );
  }

  @Test
  public void invariant_condition_throws_exception()
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
  public void invariant_checkInvariants_false()
  {
    ArezTestUtil.noCheckInvariants();

    // No failure
    Guards.invariant( () -> false, () -> "My Failure Reason" );
  }

  @Test
  public void apiInvariant_passed()
  {
    Guards.apiInvariant( () -> true, () -> "My Failure Reason" );
  }

  @Test
  public void apiInvariant_failed()
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.apiInvariant( () -> false, () -> "My Failure Reason" ) );

    assertEquals( exception.getMessage(), "My Failure Reason" );
  }

  @Test
  public void apiInvariant_condition_throws_exception()
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.apiInvariant( () -> {
        throw new RuntimeException( "X" );
      }, () -> "My Failure Reason" ) );

    final String message = exception.getMessage();
    assertTrue( message.startsWith( "Error checking condition.\n" +
                                    "Message: My Failure Reason\n" +
                                    "Throwable:\n" +
                                    "java.lang.RuntimeException: X" ) );
  }

  @Test
  public void apiInvariant_noCheckApiInvariants()
  {
    ArezTestUtil.noCheckApiInvariants();

    // No failure
    Guards.apiInvariant( () -> false, () -> "My Failure Reason" );
  }

  @Test
  public void apiInvariant_noCheckInvariants()
  {
    ArezTestUtil.noCheckInvariants();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Guards.apiInvariant( () -> false, () -> "My Failure Reason" ) );

    assertEquals( exception.getMessage(), "My Failure Reason" );
  }
}
