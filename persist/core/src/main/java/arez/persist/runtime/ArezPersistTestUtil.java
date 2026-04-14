package arez.persist.runtime;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class for interacting with ArezPersist config settings in tests.
 */
@SuppressWarnings( "WeakerAccess" )
@GwtIncompatible
public final class ArezPersistTestUtil
{
  private ArezPersistTestUtil()
  {
  }

  /**
   * Interface to intercept log messages emitted by ArezPersist runtime.
   */
  public interface Logger
  {
    void log( @Nonnull String message, @Nullable Throwable throwable );
  }

  /**
   * Reset the state of ArezPersist config to either production or development state.
   *
   * @param production true to set it to production environment configuration, false to set it to development environment config.
   */
  public static void resetConfig( final boolean production )
  {
    if ( ArezPersistConfig.isProductionEnvironment() )
    {
      throw new IllegalStateException( "Unable to reset config as ArezPersist is in production mode" );
    }

    if ( production )
    {
      noCheckApiInvariants();
    }
    else
    {
      checkApiInvariants();
    }
    enableApplicationStore();
    resetState();
  }

  /**
   * Reset the state of ArezPersist.
   * This occasionally needs to be invoked after changing configuration settings in tests.
   */
  public static void resetState()
  {
    setLogger( null );
    Registry.reset();
  }

  /**
   * Specify logger to use to capture logging in tests
   *
   * @param logger the logger.
   */
  public static void setLogger( @Nullable final Logger logger )
  {
    if ( ArezPersistConfig.isProductionEnvironment() )
    {
      throw new IllegalStateException( "Unable to call ArezTestUtil.setLogger() as ArezPersist is in production mode" );
    }

    final LogUtil.ProxyLogger proxyLogger = (LogUtil.ProxyLogger) LogUtil.getLogger();
    proxyLogger.setLogger( null == logger ? null : logger::log );
  }

  /**
   * Set the `arez.persist.enable_application_store` setting to true.
   */
  public static void enableApplicationStore()
  {
    setEnableApplicationStore( true );
  }

  /**
   * Set the `arez.persist.enable_application_store` setting to false.
   */
  public static void disableApplicationStore()
  {
    setEnableApplicationStore( false );
  }

  /**
   * Configure the `arez.persist.enable_application_store` setting.
   *
   * @param setting the setting.
   */
  private static void setEnableApplicationStore( final boolean setting )
  {
    setConstant( "ENABLE_APPLICATION_STORE", setting );
  }

  /**
   * Set the `arez.persist.check_api_invariants` setting to true.
   */
  public static void checkApiInvariants()
  {
    setCheckApiInvariants( true );
  }

  /**
   * Set the `arez.persist.check_api_invariants` setting to false.
   */
  public static void noCheckApiInvariants()
  {
    setCheckApiInvariants( false );
  }

  /**
   * Configure the `arez.persist.check_api_invariants` setting.
   *
   * @param setting the setting.
   */
  private static void setCheckApiInvariants( final boolean setting )
  {
    setConstant( "CHECK_API_INVARIANTS", setting );
  }

  /**
   * Set the specified field name on ArezPersistConfig.
   */
  @SuppressWarnings( "NonJREEmulationClassesInClientCode" )
  private static void setConstant( @Nonnull final String fieldName, final boolean value )
  {
    if ( ArezPersistConfig.isProductionEnvironment() )
    {
      throw new IllegalStateException( "Unable to change constant " + fieldName +
                                       " as ArezPersist is in production mode" );
    }
    else
    {
      try
      {
        final Field field = ArezPersistConfig.class.getDeclaredField( fieldName );
        field.setAccessible( true );
        field.set( null, value );
      }
      catch ( final NoSuchFieldException | IllegalAccessException e )
      {
        throw new IllegalStateException( "Unable to change constant " + fieldName, e );
      }
    }
  }
}
