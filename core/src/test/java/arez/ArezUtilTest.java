package arez;

import java.io.IOException;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ArezUtilTest
{
  @Test
  public void safeGetString()
    throws Exception
  {
    assertEquals( ArezUtil.safeGetString( () -> "My String" ), "My String" );
  }

  @Test
  public void safeGetString_generatesError()
    throws Exception
  {
    final String text = ArezUtil.safeGetString( () -> {
      throw new RuntimeException( "X" );
    } );
    assertTrue( text.startsWith( "Exception generated whilst attempting to get supplied message.\n" +
                                 "java.lang.RuntimeException: X\n" ) );
  }

  @Test
  public void throwableToString()
    throws Exception
  {
    final String text = ArezUtil.throwableToString( new RuntimeException( "X" ) );
    assertTrue( text.startsWith( "java.lang.RuntimeException: X\n" ) );
  }

  @Test
  public void throwableToString_with_NestedThrowable()
    throws Exception
  {
    final RuntimeException exception =
      new RuntimeException( "X", new IOException( "Y" ) );
    final String text = ArezUtil.throwableToString( exception );
    assertTrue( text.startsWith( "java.lang.RuntimeException: X\n" ) );
    assertTrue( text.contains( "\nCaused by: java.io.IOException: Y\n" ) );
  }
}
