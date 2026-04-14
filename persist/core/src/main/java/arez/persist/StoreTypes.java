package arez.persist;

import arez.persist.runtime.ArezPersist;
import arez.persist.runtime.browser.ArezPersistBrowserUtil;
import javax.annotation.Nonnull;

/**
 * A class containing constants for persistence stores supplied by the library.
 * Other persistence stores are possible but must be explicitly registered by the
 * developer.
 */
public final class StoreTypes
{
  /**
   * The property is persisted in memory and will be lost when the application is reloaded.
   * This persist strategy is only available when {@link ArezPersist#isApplicationStoreEnabled()}
   * returns {@code true}.
   */
  @Nonnull
  public static final String APPLICATION = "app";
  /**
   * The property is persisted across the session. i.e. The value of the property will be
   * persisted across reloads within the same tab. This persist strategy is only available
   * when {@link ArezPersistBrowserUtil#registerSessionStore(String)} has been invoked.
   */
  @Nonnull
  public static final String SESSION = "session";
  /**
   * The property is persisted when using the same browser. i.e. The value of the property will be
   * persisted across reloads within the same browser. If multiple browsers are open and concurrently
   * persisting storage then they may overwrite each other and the last value persisted "wins".
   * This persist strategy is only available when {@link ArezPersistBrowserUtil#registerLocalStore(String)}
   * has been invoked.
   */
  @Nonnull
  public static final String LOCAL = "local";

  private StoreTypes()
  {
  }
}
