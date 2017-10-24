package org.realityforge.arez;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
   * Clear the context provider for arez.
   * This forcibly overwrites provider and should not be done outside tests.
   */
  public static void clearProvider()
  {
    assert !ArezConfig.isProductionmode();
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
    assert !ArezConfig.isProductionmode();
    try
    {
      final Field field = ArezConfig.class.getDeclaredField( fieldName );
      field.setAccessible( true );
      field.set( null, value );
    }
    catch ( NoSuchFieldException | IllegalAccessException e )
    {
      throw new IllegalStateException( "Unable to change constant " + fieldName, e );
    }
  }
}
