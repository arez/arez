package org.realityforge.arez;

import javax.annotation.Nonnull;

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
      if ( t instanceof com.google.gwt.event.shared.UmbrellaException )
      {
        for ( final Throwable t2 : ( (com.google.gwt.event.shared.UmbrellaException) t ).getCauses() )
        {
          addCausedByPrefix( sb );
          sb.append( t2.toString() );
          sb.append( "\n  at " );
          sb.append( throwableToString( t2 ) );
        }
      }
      else if ( t instanceof com.google.web.bindery.event.shared.UmbrellaException )
      {
        for ( final Throwable t2 : ( (com.google.web.bindery.event.shared.UmbrellaException) t ).getCauses() )
        {
          addCausedByPrefix( sb );
          sb.append( t2.toString() );
          sb.append( "\n  at " );
          sb.append( throwableToString( t2 ) );
        }
      }
      else
      {
        addCausedByPrefix( sb );
        sb.append( t.toString() );
        for ( final StackTraceElement element : t.getStackTrace() )
        {
          sb.append( "\n  at " ).append( element );
        }
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
