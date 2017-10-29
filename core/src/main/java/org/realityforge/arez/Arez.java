package org.realityforge.arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.TestOnly;
import static org.realityforge.braincheck.Guards.*;

/**
 * Provide access to an instance of ArezContext and Arez global configuration settings.
 * The {@link #context()} should always return the current context. If {@link #areZonesEnabled()}
 * is false it will return a singleton otherwise the appropriate context for the zone will be
 * invoked.
 */
public final class Arez
{
  @Nullable
  private static ArezContext c_context = Arez.areZonesEnabled() ? null : new ArezContext();
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
    return ArezConfig.enableZones();
  }

  /**
   * Return true if user should pass names into API methods, false if should pass null.
   *
   * @return true if user should pass names into API methods, false if should pass null.
   */
  public static boolean areNamesEnabled()
  {
    return ArezConfig.enableNames();
  }

  /**
   * Return true if spies are enabled.
   *
   * @return true if spies are enabled, false otherwise.
   */
  public static boolean areSpiesEnabled()
  {
    return ArezConfig.enableSpy();
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
      assert null != c_context;
      return c_context;
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
    apiInvariant( Arez::areZonesEnabled, () -> "Invoked Arez.createZone() but zones are not enabled." );
    return new Zone();
  }

  @SuppressWarnings( "ConstantConditions" )
  static void activateZone( @Nonnull final Zone zone )
  {
    invariant( Arez::areZonesEnabled, () -> "Invoked Arez.activateZone() but zones are not enabled." );
    assert null != c_zoneStack;
    assert null != zone;
    c_zoneStack.add( c_zone );
    c_zone = zone;
  }

  @SuppressWarnings( "ConstantConditions" )
  static void deactivateZone( @Nonnull final Zone zone )
  {
    invariant( Arez::areZonesEnabled, () -> "Invoked Arez.deactivateZone() but zones are not enabled." );
    apiInvariant( () -> c_zone == zone, () -> "Attempted to deactivate zone that is not active." );
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
    invariant( Arez::areZonesEnabled, () -> "Invoked Arez.currentZone() but zones are not enabled." );
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
    c_context = new ArezContext();
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
