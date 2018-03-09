package arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.TestOnly;
import org.realityforge.braincheck.BrainCheckConfig;
import static org.realityforge.braincheck.Guards.*;

/**
 * Provide access to an instance of ArezContext and Arez global configuration settings.
 * The {@link #context()} should always return the current context. If {@link #areZonesEnabled()}
 * is false it will return a singleton otherwise the appropriate context for the zone will be
 * invoked.
 */
public final class Arez
{
  /**
   * Default zone if zones are enabled.
   */
  @Nullable
  private static Zone c_defaultZone = Arez.areZonesEnabled() ? new Zone() : null;
  /**
   * Default zone if zones are enabled.
   */
  @Nullable
  private static Zone c_zone = Arez.areZonesEnabled() ? c_defaultZone : null;
  /**
   * The zones that were previously active.
   * If there is no zones in the stack and a zone is deactivated then the default zone is made current.
   */
  @Nullable
  private static ArrayList<Zone> c_zoneStack = Arez.areZonesEnabled() ? new ArrayList<>() : null;

  private Arez()
  {
  }

  /**
   * Return true if zones are enabled, false otherwise.
   *
   * @return true if zones are enabled, false otherwise.
   */
  public static boolean areZonesEnabled()
  {
    return ArezConfig.areZonesEnabled();
  }

  /**
   * Return true if user should pass names into API methods, false if should pass null.
   *
   * @return true if user should pass names into API methods, false if should pass null.
   */
  public static boolean areNamesEnabled()
  {
    return ArezConfig.areNamesEnabled();
  }

  /**
   * Return true if spies are enabled.
   *
   * @return true if spies are enabled, false otherwise.
   */
  public static boolean areSpiesEnabled()
  {
    /*
     * Spy's use debug names so we can not enable spies without names.
     */
    return areNamesEnabled() && ArezConfig.areSpiesEnabled();
  }

  /**
   * Return false if collections returned by generated repositories are wrapped in unmodifiable prior to returning.
   *
   * @return false if collections returned by generated repositories are wrapped in unmodifiable prior to returning, true otherwise.
   */
  public static boolean areRepositoryResultsModifiable()
  {
    return ArezConfig.areRepositoryResultsModifiable();
  }

  /**
   * Return true if property introspectors for Observables are enabled.
   *
   * @return true if property introspectors for Observables are enabled, false otherwise.
   */
  public static boolean arePropertyIntrospectorsEnabled()
  {
    return ArezConfig.arePropertyIntrospectorsEnabled();
  }

  /**
   * Return true if registries for top level reactive components are enabled.
   *
   * @return true if registries for top level reactive components are enabled, false otherwise.
   */
  public static boolean areRegistriesEnabled()
  {
    return areNamesEnabled() && ArezConfig.areRegistriesEnabled();
  }

  /**
   * Return true if native components are enabled.
   *
   * @return true if native components are enabled, false otherwise.
   */
  public static boolean areNativeComponentsEnabled()
  {
    return ArezConfig.areNativeComponentsEnabled();
  }

  /**
   * Return true if Arez should enforce transaction modes.
   *
   * @return true if Arez should enforce transaction modes.
   */
  static boolean shouldEnforceTransactionType()
  {
    return ArezConfig.enforceTransactionType();
  }

  /**
   * Return true if invariants will be checked.
   *
   * @return true if invariants will be checked.
   */
  public static boolean shouldCheckInvariants()
  {
    return ArezConfig.checkInvariants() && BrainCheckConfig.checkInvariants();
  }

  /**
   * Return true if apiInvariants will be checked.
   *
   * @return true if apiInvariants will be checked.
   */
  public static boolean shouldCheckApiInvariants()
  {
    return ArezConfig.checkApiInvariants() && BrainCheckConfig.checkApiInvariants();
  }

  /**
   * Return the ArezContext from the provider.
   *
   * @return the ArezContext.
   */
  @Nonnull
  public static ArezContext context()
  {
    if ( areZonesEnabled() )
    {
      assert null != c_zone;
      return c_zone.getContext();
    }
    else
    {
      return ArezContextHolder.context();
    }
  }

  /**
   * Create a new zone.
   * This zone is not yet activated.
   *
   * @return the new zone.
   */
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
  @SuppressWarnings( "ConstantConditions" )
  static void activateZone( @Nonnull final Zone zone )
  {
    if ( shouldCheckInvariants() )
    {
      invariant( Arez::areZonesEnabled, () -> "Arez-0002: Invoked Arez.activateZone() but zones are not enabled." );
    }
    assert null != c_zoneStack;
    assert null != zone;
    c_zoneStack.add( c_zone );
    c_zone = zone;
  }

  /**
   * Restore the old zone.
   * This takes the zone that was current when {@link #activateZone(Zone)} was called for the active zone
   * and restores it to being the current zone.
   */
  @SuppressWarnings( "ConstantConditions" )
  static void deactivateZone( @Nonnull final Zone zone )
  {
    if ( shouldCheckInvariants() )
    {
      invariant( Arez::areZonesEnabled, () -> "Arez-0003: Invoked Arez.deactivateZone() but zones are not enabled." );
    }
    if ( shouldCheckApiInvariants() )
    {
      apiInvariant( () -> c_zone == zone, () -> "Arez-0004: Attempted to deactivate zone that is not active." );
    }
    assert null != c_zoneStack;
    c_zone = c_zoneStack.isEmpty() ? c_defaultZone : c_zoneStack.remove( c_zoneStack.size() - 1 );
  }

  /**
   * Return the current zone.
   *
   * @return the current zone.
   */
  @Nonnull
  static Zone currentZone()
  {
    if ( shouldCheckInvariants() )
    {
      invariant( Arez::areZonesEnabled, () -> "Arez-0005: Invoked Arez.currentZone() but zones are not enabled." );
    }
    assert null != c_zone;
    return c_zone;
  }

  /**
   * Clear the state to cleanup .
   * This is dangerous as it may leave dangling references and should only be done in tests.
   */
  @TestOnly
  static void reset()
  {
    c_defaultZone = new Zone();
    c_zone = c_defaultZone;
    ArezContextHolder.reset();
    c_zoneStack = new ArrayList<>();
  }

  @TestOnly
  @Nonnull
  static Zone getDefaultZone()
  {
    assert null != c_defaultZone;
    return c_defaultZone;
  }

  @TestOnly
  @Nonnull
  static ArrayList<Zone> getZoneStack()
  {
    assert null != c_zoneStack;
    return c_zoneStack;
  }
}
