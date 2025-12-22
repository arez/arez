package arez;

import grim.annotations.OmitSymbol;
import grim.annotations.OmitType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A utility class that contains reference to zone data when zones are enabled.
 * This is extracted to a separate class to eliminate the <clinit> from Arez and thus
 * make it much easier for GWT to optimize out code based on build time compilation parameters.
 */
@OmitType( unless = "arez.enable_zones" )
final class ZoneHolder
{
  /**
   * The next numeric suffix for auto-generated zone names.
   */
  private static int c_nextZoneId = 1;
  /**
   * Default zone if zones are enabled.
   */
  @Nullable
  private static Zone c_defaultZone = Arez.areZonesEnabled() ? createZone( null ) : null;
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
  private static List<Zone> c_zoneStack = Arez.areZonesEnabled() ? new ArrayList<>() : null;

  private ZoneHolder()
  {
  }

  /**
   * Create a new zone, generating a name if appropriate.
   *
   * @param name the optional name.
   * @return the new zone instance.
   */
  @Nonnull
  static Zone createZone( @Nullable final String name )
  {
    assert Arez.areZonesEnabled();
    return new Zone( Arez.areNamesEnabled() ? null != name ? name : "Zone@" + c_nextZoneId++ : null );
  }

  /**
   * Return the ArezContext from the provider.
   *
   * @return the ArezContext.
   */
  @Nonnull
  static ArezContext context()
  {
    assert null != c_zone;
    return c_zone.getContext();
  }

  /**
   * Save the old zone and make the specified zone the current zone.
   */
  @SuppressWarnings( "ConstantConditions" )
  static void activateZone( @Nonnull final Zone zone )
  {
    if ( Arez.shouldCheckInvariants() )
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
  static void deactivateZone( @Nonnull final Zone zone )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areZonesEnabled, () -> "Arez-0003: Invoked Arez.deactivateZone() but zones are not enabled." );
    }
    if ( Arez.shouldCheckApiInvariants() )
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
    if ( Arez.shouldCheckInvariants() )
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
  @OmitSymbol
  static void reset()
  {
    c_nextZoneId = 1;
    c_defaultZone = Arez.areZonesEnabled() ? createZone( null ) : null;
    c_zone = c_defaultZone;
    c_zoneStack = Arez.areZonesEnabled() ? new ArrayList<>() : null;
  }

  @OmitSymbol
  @Nonnull
  static Zone getDefaultZone()
  {
    assert null != c_defaultZone;
    return c_defaultZone;
  }

  @OmitSymbol
  @Nonnull
  static List<Zone> getZoneStack()
  {
    assert null != c_zoneStack;
    return c_zoneStack;
  }
}
