package org.realityforge.arez;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ArezTest
  extends AbstractArezTest
{
  @Test
  public void context_when_zones_disabled()
  {
    ArezTestUtil.setEnableZones( false );

    final ArezContext context1 = Arez.context();
    assertNotNull( context1 );
    final ArezContext context2 = Arez.context();
    assertTrue( context1 == context2 );
  }

  @Test
  public void zone_basicOperation()
  {
    ArezTestUtil.setEnableZones( true );

    assertEquals( Arez.getDefaultZone().getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 0 );

    final Zone zone1 = Arez.createZone();

    assertEquals( Arez.getDefaultZone().getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 0 );
    assertEquals( zone1.isActive(), false );

    zone1.activate();

    assertEquals( zone1.getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 1 );
    assertEquals( zone1.isActive(), true );

    zone1.deactivate();

    assertEquals( Arez.getDefaultZone().getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 0 );
  }

  @Test
  public void zone_multipleZones()
  {
    ArezTestUtil.setEnableZones( true );

    assertEquals( Arez.getDefaultZone().getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 0 );

    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();
    final Zone zone3 = Arez.createZone();

    assertEquals( Arez.getDefaultZone().getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 0 );
    assertEquals( zone1.isActive(), false );
    assertEquals( zone2.isActive(), false );
    assertEquals( zone3.isActive(), false );

    zone1.activate();

    assertEquals( zone1.getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 1 );
    assertEquals( zone1.isActive(), true );
    assertEquals( zone2.isActive(), false );
    assertEquals( zone3.isActive(), false );

    zone2.activate();

    assertEquals( zone2.getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 2 );
    assertEquals( Arez.getZoneStack().get( 0 ), Arez.getDefaultZone() );
    assertEquals( Arez.getZoneStack().get( 1 ), zone1 );
    assertEquals( zone1.isActive(), false );
    assertEquals( zone2.isActive(), true );
    assertEquals( zone3.isActive(), false );

    zone1.activate();

    assertEquals( zone1.getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 3 );
    assertEquals( Arez.getZoneStack().get( 0 ), Arez.getDefaultZone() );
    assertEquals( Arez.getZoneStack().get( 1 ), zone1 );
    assertEquals( Arez.getZoneStack().get( 2 ), zone2 );
    assertEquals( zone1.isActive(), true );
    assertEquals( zone2.isActive(), false );
    assertEquals( zone3.isActive(), false );

    zone3.activate();

    assertEquals( zone3.getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 4 );
    assertEquals( Arez.getZoneStack().get( 0 ), Arez.getDefaultZone() );
    assertEquals( Arez.getZoneStack().get( 1 ), zone1 );
    assertEquals( Arez.getZoneStack().get( 2 ), zone2 );
    assertEquals( Arez.getZoneStack().get( 3 ), zone1 );
    assertEquals( zone1.isActive(), false );
    assertEquals( zone2.isActive(), false );
    assertEquals( zone3.isActive(), true );

    zone3.deactivate();

    assertEquals( zone1.getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 3 );
    assertEquals( Arez.getZoneStack().get( 0 ), Arez.getDefaultZone() );
    assertEquals( Arez.getZoneStack().get( 1 ), zone1 );
    assertEquals( Arez.getZoneStack().get( 2 ), zone2 );
    assertEquals( zone1.isActive(), true );
    assertEquals( zone2.isActive(), false );
    assertEquals( zone3.isActive(), false );

    zone1.deactivate();

    assertEquals( zone2.getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 2 );
    assertEquals( Arez.getZoneStack().get( 0 ), Arez.getDefaultZone() );
    assertEquals( Arez.getZoneStack().get( 1 ), zone1 );
    assertEquals( zone1.isActive(), false );
    assertEquals( zone2.isActive(), true );
    assertEquals( zone3.isActive(), false );

    zone2.deactivate();

    assertEquals( zone1.getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 1 );
    assertEquals( Arez.getZoneStack().get( 0 ), Arez.getDefaultZone() );
    assertEquals( zone1.isActive(), true );
    assertEquals( zone2.isActive(), false );
    assertEquals( zone3.isActive(), false );

    zone1.deactivate();

    assertEquals( Arez.getDefaultZone().getContext(), Arez.context() );
    assertEquals( Arez.getZoneStack().size(), 0 );
    assertEquals( zone1.isActive(), false );
    assertEquals( zone2.isActive(), false );
    assertEquals( zone3.isActive(), false );
  }

  @Test
  public void createZone_when_zonesDisabled()
  {
    ArezTestUtil.setEnableZones( false );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, Arez::createZone );
    assertEquals( exception.getMessage(), "Invoked Arez.createZone() but zones are not enabled." );
  }

  @Test
  public void activateZone_whenZonesNotEnabled()
  {
    ArezTestUtil.setEnableZones( false );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Arez.activateZone( new Zone() ) );
    assertEquals( exception.getMessage(), "Invoked Arez.activateZone() but zones are not enabled." );
  }

  @Test
  public void deactivateZone_whenZonesNotEnabled()
  {
    ArezTestUtil.setEnableZones( false );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Arez.deactivateZone( new Zone() ) );
    assertEquals( exception.getMessage(), "Invoked Arez.deactivateZone() but zones are not enabled." );
  }

  @Test
  public void currentZone_whenZonesNotEnabled()
  {
    ArezTestUtil.setEnableZones( false );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, Arez::currentZone );
    assertEquals( exception.getMessage(), "Invoked Arez.currentZone() but zones are not enabled." );
  }

  @Test
  public void deactivateZone_whenNotActive()
  {
    ArezTestUtil.setEnableZones( true );
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> Arez.deactivateZone( new Zone() ) );
    assertEquals( exception.getMessage(), "Attempted to deactivate zone that is not active." );
  }
}
