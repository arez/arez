package org.realityforge.arez;

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
}
