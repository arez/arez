package arez.spytools.browser.react4j;

import arez.Arez;
import javax.annotation.Nullable;

/**
 * Utility class for interacting with spy capabilities.
 */
public final class ReactArezSpyUtil
{
  @Nullable
  private static final ReactArezConsoleSpyEventProcessor PROCESSOR =
    Arez.areSpiesEnabled() ? new ReactArezConsoleSpyEventProcessor() : null;
  private static boolean c_loggingEnabled;

  /**
   * Return true if spy event logging is enabled.
   *
   * @return true if spy event logging is enabled.
   */
  public static boolean isSpyEventLoggingEnabled()
  {
    return Arez.areSpiesEnabled() && c_loggingEnabled;
  }

  /**
   * Enable console logging of all spy events.
   * This is a noop if spies are not enabled or logging has already been enabled.
   */
  public static void enableSpyEventLogging()
  {
    if ( Arez.areSpiesEnabled() && !isSpyEventLoggingEnabled() )
    {
      Arez.context().getSpy().addSpyEventHandler( PROCESSOR );
      c_loggingEnabled = true;
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
      Arez.context().getSpy().removeSpyEventHandler( PROCESSOR );
      c_loggingEnabled = false;
    }
  }

  private ReactArezSpyUtil()
  {
  }
}
