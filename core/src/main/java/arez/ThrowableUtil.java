package arez;

import javax.annotation.Nonnull;

/**
 * This class uses explicit traversal to be compatible with GWT.
 *
 * Contrast this with the following code that is not compatible with GWT.
 * <pre>
 * final StringWriter out = new StringWriter();
 * t.printStackTrace( new PrintWriter( out ) );
 * </pre>
 */
final class ThrowableUtil
{
  public ThrowableUtil()
  {
  }

  /**
   * Return string converted to stack trace.
   *
   * @param throwable the throwable to convert
   * @return the stack trace.
   */
  @Nonnull
  static String throwableToString( @Nonnull final Throwable throwable )
  {
    final StringBuilder sb = new StringBuilder();
    Throwable t = throwable;
    while ( null != t )
    {
      addCausedByPrefix( sb );
      sb.append( t.toString() );
      for ( final StackTraceElement element : t.getStackTrace() )
      {
        sb.append( "\n  at " ).append( element );
      }
      t = t.getCause();
    }

    return sb.toString();
  }

  private static void addCausedByPrefix( @Nonnull final StringBuilder sb )
  {
    if ( 0 != sb.length() )
    {
      sb.append( "\nCaused by: " );
    }
  }
}
