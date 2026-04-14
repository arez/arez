package arez.spytools.browser.react4j;

import arez.Arez;
import arez.spy.SpyEventHandler;
import arez.spytools.browser.BrowserSpyUtil;
import javax.annotation.Nullable;

/**
 * Utility class for interacting with spy capabilities.
 */
public final class ReactArezSpyUtil
{
  @Nullable
  private static final SpyEventHandler PROCESSOR =
    Arez.areSpiesEnabled() ? new ReactArezConsoleSpyEventProcessor() : null;

  /**
   * Return true if spy event logging is enabled.
   *
   * @return true if spy event logging is enabled.
   */
  public static boolean isSpyEventLoggingEnabled()
  {
    return BrowserSpyUtil.isSpyEventLoggingEnabled();
  }

  /**
   * Enable console logging of all spy events.
   * This is a noop if spies are not enabled or logging has already been enabled.
   */
  public static void enableSpyEventLogging()
  {
    BrowserSpyUtil.enableSpyEventLogging( PROCESSOR );
  }

  /**
   * Disable console logging of all spy events.
   * This is a noop if spies are not enabled or logging is not enabled.
   */
  public static void disableSpyEventLogging()
  {
    BrowserSpyUtil.disableSpyEventLogging();
  }

  private ReactArezSpyUtil()
  {
  }
}
