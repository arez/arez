package arez;

import java.io.IOException;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ThrowableUtilTest
{
  @Test
  public void throwableToString()
    throws Exception
  {
    final String text = ThrowableUtil.throwableToString( new RuntimeException( "X" ) );
    assertTrue( text.startsWith( "java.lang.RuntimeException: X\n" ) );
  }

  @Test
  public void throwableToString_with_NestedThrowable()
    throws Exception
  {
    final RuntimeException exception =
      new RuntimeException( "X", new IOException( "Y" ) );
    final String text = ThrowableUtil.throwableToString( exception );
    assertTrue( text.startsWith( "java.lang.RuntimeException: X\n" ) );
    assertTrue( text.contains( "\nCaused by: java.io.IOException: Y\n" ) );
  }
}
