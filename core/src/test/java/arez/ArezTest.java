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
    assertSame( context1, context2 );
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
    assertFalse( zone1.isActive() );

    zone1.safeRun( () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ArezZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
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
    assertFalse( zone1.isActive() );
    assertFalse( zone2.isActive() );
    assertFalse( zone3.isActive() );

    zone1.safeRun( () -> {

      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ArezZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
      assertFalse( zone2.isActive() );
      assertFalse( zone3.isActive() );

      zone2.safeRun( () -> {

        assertEquals( zone2.getContext(), Arez.context() );
        assertEquals( ArezZoneHolder.getZoneStack().size(), 2 );
        assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
        assertEquals( ArezZoneHolder.getZoneStack().get( 1 ), zone1 );
        assertFalse( zone1.isActive() );
        assertTrue( zone2.isActive() );
        assertFalse( zone3.isActive() );

        zone1.safeRun( () -> {

          assertEquals( zone1.getContext(), Arez.context() );
          assertEquals( ArezZoneHolder.getZoneStack().size(), 3 );
          assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
          assertEquals( ArezZoneHolder.getZoneStack().get( 1 ), zone1 );
          assertEquals( ArezZoneHolder.getZoneStack().get( 2 ), zone2 );
          assertTrue( zone1.isActive() );
          assertFalse( zone2.isActive() );
          assertFalse( zone3.isActive() );

          zone3.safeRun( () -> {

            assertEquals( zone3.getContext(), Arez.context() );
            assertEquals( ArezZoneHolder.getZoneStack().size(), 4 );
            assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
            assertEquals( ArezZoneHolder.getZoneStack().get( 1 ), zone1 );
            assertEquals( ArezZoneHolder.getZoneStack().get( 2 ), zone2 );
            assertEquals( ArezZoneHolder.getZoneStack().get( 3 ), zone1 );
            assertFalse( zone1.isActive() );
            assertFalse( zone2.isActive() );
            assertTrue( zone3.isActive() );

          } );

          assertEquals( zone1.getContext(), Arez.context() );
          assertEquals( ArezZoneHolder.getZoneStack().size(), 3 );
          assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
          assertEquals( ArezZoneHolder.getZoneStack().get( 1 ), zone1 );
          assertEquals( ArezZoneHolder.getZoneStack().get( 2 ), zone2 );
          assertTrue( zone1.isActive() );
          assertFalse( zone2.isActive() );
          assertFalse( zone3.isActive() );

        } );

        assertEquals( zone2.getContext(), Arez.context() );
        assertEquals( ArezZoneHolder.getZoneStack().size(), 2 );
        assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
        assertEquals( ArezZoneHolder.getZoneStack().get( 1 ), zone1 );
        assertFalse( zone1.isActive() );
        assertTrue( zone2.isActive() );
        assertFalse( zone3.isActive() );

      } );

      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ArezZoneHolder.getZoneStack().size(), 1 );
      assertEquals( ArezZoneHolder.getZoneStack().get( 0 ), ArezZoneHolder.getDefaultZone() );
      assertTrue( zone1.isActive() );
      assertFalse( zone2.isActive() );
      assertFalse( zone3.isActive() );

    } );

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );
    assertFalse( zone2.isActive() );
    assertFalse( zone3.isActive() );
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
