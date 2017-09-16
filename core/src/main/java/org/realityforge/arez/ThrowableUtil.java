package org.realityforge.arez;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.annotation.Nonnull;

final class ThrowableUtil
{
  public ThrowableUtil()
  {
  }

  /**
   * Return string converted to stack trace.
   *
   * @param t the throwable to convert
   * @return the stack trace.
   */
  @Nonnull
  static String throwableToString( @Nonnull final Throwable t )
  {
    final StringWriter out = new StringWriter();
    t.printStackTrace( new PrintWriter( out ) );
    return out.toString();
  }
}
