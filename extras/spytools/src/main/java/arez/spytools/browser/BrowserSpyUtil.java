package arez.spytools.browser;

import arez.Arez;
import arez.ArezContext;
import arez.spy.SpyEventHandler;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class for interacting with spy capabilities.
 */
public final class BrowserSpyUtil
{
  @Nonnull
  private static final Map<ArezContext, SpyEventHandler> c_processors = Arez.areSpiesEnabled() ? new HashMap<>() : null;

  /**
   * Return true if spy event logging is enabled.
   *
   * @return true if spy event logging is enabled.
   */
  public static boolean isSpyEventLoggingEnabled()
  {
    return Arez.areSpiesEnabled() && c_processors.containsKey( Arez.context() );
  }

  /**
   * Enable console logging of all spy events.
   * This is a noop if spies are not enabled or logging has already been enabled.
   */
  public static void enableSpyEventLogging()
  {
    enableSpyEventLogging( Arez.areSpiesEnabled() && !isSpyEventLoggingEnabled() ?
                           new ConsoleSpyEventProcessor() :
                           null );
  }

  /**
   * Enable console logging of all spy events.
   * This is a noop if spies are not enabled or logging has already been enabled.
   */
  public static void enableSpyEventLogging( @Nullable final SpyEventHandler handler )
  {
    if ( Arez.areSpiesEnabled() && !isSpyEventLoggingEnabled() )
    {
      final SpyEventHandler actualHandler = null == handler ? new ConsoleSpyEventProcessor() : handler;
      final ArezContext context = Arez.context();
      context.getSpy().addSpyEventHandler( actualHandler );
      c_processors.put( context, actualHandler );
    }
  }

  /**
   * Disable console logging of all spy events.
   * This is a noop if spies are not enabled or logging is not enabled.
   */
  public static void disableSpyEventLogging()
  {
    if ( Arez.areSpiesEnabled() && isSpyEventLoggingEnabled() )
    {
      final ArezContext context = Arez.context();
      final SpyEventHandler handler = c_processors.remove( context );
      assert null != handler;
      context.getSpy().removeSpyEventHandler( handler );
    }
  }

  private BrowserSpyUtil()
  {
  }
}
