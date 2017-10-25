package org.realityforge.arez;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import org.realityforge.anodoc.TestOnly;

/**
 * Utility class for interacting with Arez config settings in tests.
 */
@TestOnly
@GwtIncompatible
public final class ArezTestUtil
{
  private ArezTestUtil()
  {
  }

  /**
   * Reset the state of Arez config to either production or development state.
   *
   * @param productionMode true to set it to production mode configuration, false to set it to development mode config.
   */
  public static void resetConfig( final boolean productionMode )
  {
    assert !ArezConfig.isProductionMode();

    if ( productionMode )
    {
      setEnableNames( false );
      setEnforceTransactionType( false );
      setEnableSpy( false );
    }
    else
    {
      setEnableNames( true );
      setEnforceTransactionType( true );
      setEnableSpy( true );
    }
    setPurgeReactionsWhenRunawayDetected( true );
    setEnableZones( false );

    ( (ArezLogger.ProxyLogger) ArezLogger.getLogger() ).setLogger( null );
    Transaction.setTransaction( null );
    clearProvider();
  }

  /**
   * Clear the context provider for arez.
   * This forcibly overwrites provider and should not be done outside tests.
   */
  public static void clearProvider()
  {
    assert !ArezConfig.isProductionMode();
    Arez.clearProvider();
  }

  /**
   * Configure the enableNames setting.
   *
   * @param value the setting.
   */
  public static void setEnableNames( final boolean value )
  {
    setConstant( "ENABLE_NAMES", value );
  }

  /**
   * Configure the "purgeReactionsWhenRunawayDetected" setting.
   *
   * @param value the setting.
   */
  public static void setPurgeReactionsWhenRunawayDetected( final boolean value )
  {
    setConstant( "PURGE_REACTIONS", value );
  }

  /**
   * Configure the "enforceTransactionType" setting.
   *
   * @param value the setting.
   */
  public static void setEnforceTransactionType( final boolean value )
  {
    setConstant( "ENFORCE_TRANSACTION_TYPE", value );
  }

  /**
   * Configure the "enableSpy" setting.
   *
   * @param value the setting.
   */
  public static void setEnableSpy( final boolean value )
  {
    setConstant( "ENABLE_SPY", value );
  }

  /**
   * Configure the enableZones setting.
   *
   * @param value the setting.
   */
  public static void setEnableZones( final boolean value )
  {
    setConstant( "ENABLE_ZONES", value );
  }

  /**
   * Set the specified field name on ArezConfig.
   */
  @SuppressWarnings( "NonJREEmulationClassesInClientCode" )
  private static void setConstant( @Nonnull final String fieldName, final boolean value )
  {
    assert !ArezConfig.isProductionMode();
    try
    {
      final Field field = ArezConfig.class.getDeclaredField( fieldName );
      field.setAccessible( true );
      field.set( null, value );
    }
    catch ( final NoSuchFieldException | IllegalAccessException e )
    {
      throw new IllegalStateException( "Unable to change constant " + fieldName, e );
    }
  }
}
