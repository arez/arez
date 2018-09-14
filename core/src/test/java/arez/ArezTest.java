package arez;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ArezTest
  extends AbstractArezTest
{
  @Test
  public void context_when_zones_disabled()
  {
    ArezTestUtil.disableZones();

    final ArezContext context1 = Arez.context();
    assertNotNull( context1 );
    final ArezContext context2 = Arez.context();
    assertTrue( context1 == context2 );
  }

  @Test
  public void zone_basicOperation()
  {
    ArezTestUtil.enableZones();
    ArezTestUtil.resetState();

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );

    final Zone zone1 = Arez.createZone();

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
    assertEquals( zone1.isActive(), false );

    zone1.safeRun( () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ArezZoneHolder.getZoneStack().size(), 1 );
      assertEquals( zone1.isActive(), true );
    } );

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
  }

  @Test
  public void zone_multipleZones()
  {
    ArezTestUtil.enableZones();
    ArezTestUtil.resetState();

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );

    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();
    final Zone zone3 = Arez.createZone();

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
    assertEquals( zone1.isActive(), false );
    assertEquals( zone2.isActive(), false );
    assertEquals( zone3.isActive(), false );

    zone1.safeRun( () -> {

      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ArezZoneHolder.getZoneStack().size(), 1 );
      assertEquals( zone1.isActive(), true );
      assertEquals( zone2.isActive(), false );
      assertEquals( zone3.isActive(), false );

      zone2.safeRun( () -> {

        assertEquals( zone2.getContext(), Arez.context() );
        assertEquals( ArezZoneHolder.getZoneStack().size(), 2 );
        assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
        assertEquals( ArezZoneHolder.getZoneStack().get( 1 ), zone1 );
        assertEquals( zone1.isActive(), false );
        assertEquals( zone2.isActive(), true );
        assertEquals( zone3.isActive(), false );

        zone1.safeRun( () -> {

          assertEquals( zone1.getContext(), Arez.context() );
          assertEquals( ArezZoneHolder.getZoneStack().size(), 3 );
          assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
          assertEquals( ArezZoneHolder.getZoneStack().get( 1 ), zone1 );
          assertEquals( ArezZoneHolder.getZoneStack().get( 2 ), zone2 );
          assertEquals( zone1.isActive(), true );
          assertEquals( zone2.isActive(), false );
          assertEquals( zone3.isActive(), false );

          zone3.safeRun( () -> {

            assertEquals( zone3.getContext(), Arez.context() );
            assertEquals( ArezZoneHolder.getZoneStack().size(), 4 );
            assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
            assertEquals( ArezZoneHolder.getZoneStack().get( 1 ), zone1 );
            assertEquals( ArezZoneHolder.getZoneStack().get( 2 ), zone2 );
            assertEquals( ArezZoneHolder.getZoneStack().get( 3 ), zone1 );
            assertEquals( zone1.isActive(), false );
            assertEquals( zone2.isActive(), false );
            assertEquals( zone3.isActive(), true );

          } );

          assertEquals( zone1.getContext(), Arez.context() );
          assertEquals( ArezZoneHolder.getZoneStack().size(), 3 );
          assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
          assertEquals( ArezZoneHolder.getZoneStack().get( 1 ), zone1 );
          assertEquals( ArezZoneHolder.getZoneStack().get( 2 ), zone2 );
          assertEquals( zone1.isActive(), true );
          assertEquals( zone2.isActive(), false );
          assertEquals( zone3.isActive(), false );

        } );

        assertEquals( zone2.getContext(), Arez.context() );
        assertEquals( ArezZoneHolder.getZoneStack().size(), 2 );
        assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
        assertEquals( ArezZoneHolder.getZoneStack().get( 1 ), zone1 );
        assertEquals( zone1.isActive(), false );
        assertEquals( zone2.isActive(), true );
        assertEquals( zone3.isActive(), false );

      } );

      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ArezZoneHolder.getZoneStack().size(), 1 );
      assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
      assertEquals( zone1.isActive(), true );
      assertEquals( zone2.isActive(), false );
      assertEquals( zone3.isActive(), false );

    } );

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
    assertEquals( zone1.isActive(), false );
    assertEquals( zone2.isActive(), false );
    assertEquals( zone3.isActive(), false );
  }

  @Test
  public void createZone_when_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    assertInvariantFailure( Arez::createZone, "Arez-0001: Invoked Arez.createZone() but zones are not enabled." );
  }

  @Test
  public void activateZone_whenZonesNotEnabled()
  {
    ArezTestUtil.disableZones();

    assertInvariantFailure( () -> Arez.activateZone( new Zone() ),
                            "Arez-0002: Invoked Arez.activateZone() but zones are not enabled." );
  }

  @Test
  public void deactivateZone_whenZonesNotEnabled()
  {
    ArezTestUtil.disableZones();

    assertInvariantFailure( () -> Arez.deactivateZone( new Zone() ),
                            "Arez-0003: Invoked Arez.deactivateZone() but zones are not enabled." );
  }

  @Test
  public void currentZone_whenZonesNotEnabled()
  {
    ArezTestUtil.disableZones();
    assertInvariantFailure( Arez::currentZone, "Arez-0005: Invoked Arez.currentZone() but zones are not enabled." );
  }

  @Test
  public void deactivateZone_whenNotActive()
  {
    ArezTestUtil.enableZones();
    ArezTestUtil.resetState();
    assertInvariantFailure( () -> Arez.deactivateZone( new Zone() ),
                            "Arez-0004: Attempted to deactivate zone that is not active." );
  }
}
