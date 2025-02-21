package arez;

import arez.component.Verifiable;
import grim.annotations.OmitClinit;
import grim.annotations.OmitSymbol;
import javax.annotation.Nonnull;
import org.realityforge.braincheck.BrainCheckConfig;
import static org.realityforge.braincheck.Guards.*;

/**
 * Provide access to an instance of ArezContext and Arez global configuration settings.
 * The {@link #context()} should always return the current context. If {@link #areZonesEnabled()}
 * is false it will return a singleton otherwise the appropriate context for the zone will be
 * invoked.
 */
@OmitClinit
public final class Arez
{
  private Arez()
  {
  }

  /**
   * Return true if zones are enabled, false otherwise.
   *
   * @return true if zones are enabled, false otherwise.
   */
  @OmitSymbol
  public static boolean areZonesEnabled()
  {
    return ArezConfig.areZonesEnabled();
  }

  /**
   * Return true if user should pass names into API methods, false if should pass null.
   *
   * @return true if user should pass names into API methods, false if should pass null.
   */
  @OmitSymbol
  public static boolean areNamesEnabled()
  {
    return ArezConfig.areNamesEnabled();
  }

  /**
   * Return true if {@link Verifiable} will verify components be used, false if not.
   *
   * @return true if {@link Verifiable} will verify components be used, false if not.
   */
  @OmitSymbol
  public static boolean isVerifyEnabled()
  {
    return ArezConfig.isVerifyEnabled();
  }

  /**
   * Return true if spies are enabled.
   *
   * @return true if spies are enabled, false otherwise.
   */
  @OmitSymbol
  public static boolean areSpiesEnabled()
  {
    /*
     * Spy's use debug names so we can not enable spies without names.
     */
    return areNamesEnabled() && ArezConfig.areSpiesEnabled();
  }

  /**
   * Return true if references are enabled.
   *
   * @return true if references are enabled, false otherwise.
   */
  @OmitSymbol
  public static boolean areReferencesEnabled()
  {
    return ArezConfig.areReferencesEnabled();
  }

  /**
   * Return true if observable properties, computable properties or query results that are of type collection are wrapped in unmodifiable variant prior to returning.
   *
   * @return true if observable properties, computable properties or query results that are of type collection are wrapped in unmodifiable variant prior to returning.
   */
  @OmitSymbol
  public static boolean areCollectionsPropertiesUnmodifiable()
  {
    return ArezConfig.areCollectionsPropertiesUnmodifiable();
  }

  /**
   * Return true if property introspectors for Observables are enabled.
   *
   * @return true if property introspectors for Observables are enabled, false otherwise.
   */
  @OmitSymbol
  public static boolean arePropertyIntrospectorsEnabled()
  {
    return areSpiesEnabled() && ArezConfig.arePropertyIntrospectorsEnabled();
  }

  /**
   * Return true if registries for top level reactive components are enabled.
   *
   * @return true if registries for top level reactive components are enabled, false otherwise.
   */
  @OmitSymbol
  public static boolean areRegistriesEnabled()
  {
    return areNamesEnabled() && ArezConfig.areRegistriesEnabled();
  }

  /**
   * Return true if native components are enabled.
   *
   * @return true if native components are enabled, false otherwise.
   */
  @OmitSymbol
  public static boolean areNativeComponentsEnabled()
  {
    return ArezConfig.areNativeComponentsEnabled();
  }

  /**
   * Return true if observer error handlers are enabled.
   *
   * @return true if observer error handlers are enabled, false otherwise.
   */
  @OmitSymbol
  public static boolean areObserverErrorHandlersEnabled()
  {
    return ArezConfig.areObserverErrorHandlersEnabled();
  }

  /**
   * Return true if Arez should enforce transaction modes.
   *
   * @return true if Arez should enforce transaction modes.
   */
  @OmitSymbol
  static boolean shouldEnforceTransactionType()
  {
    return ArezConfig.enforceTransactionType();
  }

  /**
   * Return true if a task interceptor can be specified.
   *
   * @return true if a task interceptor can be specified
   */
  @OmitSymbol
  public static boolean isTaskInterceptorEnabled()
  {
    return ArezConfig.isTaskInterceptorEnabled();
  }

  /**
   * Return true if invariants will be checked.
   *
   * @return true if invariants will be checked.
   */
  @OmitSymbol
  public static boolean shouldCheckInvariants()
  {
    return ArezConfig.checkInvariants() && BrainCheckConfig.checkInvariants();
  }

  /**
   * Return true if expensive invariants will be checked.
   *
   * @return true if expensive invariants will be checked.
   */
  @OmitSymbol
  public static boolean shouldCheckExpensiveInvariants()
  {
    return ArezConfig.checkExpensiveInvariants() && BrainCheckConfig.checkInvariants();
  }

  /**
   * Return true if apiInvariants will be checked.
   *
   * @return true if apiInvariants will be checked.
   */
  @OmitSymbol
  public static boolean shouldCheckApiInvariants()
  {
    return ArezConfig.checkApiInvariants() && BrainCheckConfig.checkApiInvariants();
  }

  /**
   * Return true if active tasks will be purged if the scheduler is still running after 100 rounds.
   *
   * @return true if active tasks will be purged if the scheduler is still running after 100 rounds.
   */
  @OmitSymbol
  public static boolean purgeTasksWhenRunawayDetected()
  {
    return ArezConfig.purgeTasksWhenRunawayDetected();
  }

  /**
   * Return the ArezContext from the provider.
   *
   * @return the ArezContext.
   */
  @Nonnull
  public static ArezContext context()
  {
    return areZonesEnabled() ? ZoneHolder.context() : ArezContextHolder.context();
  }

  /**
   * Create a new zone.
   * This zone is not yet activated.
   *
   * @return the new zone.
   */
  @OmitSymbol( unless = "arez.enable_zones" )
  @Nonnull
  public static Zone createZone()
  {
    if ( shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areZonesEnabled, () -> "Arez-0001: Invoked Arez.createZone() but zones are not enabled." );
    }
    return new Zone();
  }

  /**
   * Save the old zone and make the specified zone the current zone.
   */
  @OmitSymbol( unless = "arez.enable_zones" )
  static void activateZone( @Nonnull final Zone zone )
  {
    ZoneHolder.activateZone( zone );
  }

  /**
   * Restore the old zone.
   * This takes the zone that was current when {@link #activateZone(Zone)} was called for the active zone
   * and restores it to being the current zone.
   */
  @OmitSymbol( unless = "arez.enable_zones" )
  static void deactivateZone( @Nonnull final Zone zone )
  {
    ZoneHolder.deactivateZone( zone );
  }

  /**
   * Return the current zone.
   *
   * @return the current zone.
   */
  @OmitSymbol( unless = "arez.enable_zones" )
  @Nonnull
  static Zone currentZone()
  {
    return ZoneHolder.currentZone();
  }
}
