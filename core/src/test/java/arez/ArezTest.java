package arez;

import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ArezTest
  extends AbstractTest
{
  @Test
  public void context_when_zones_disabled()
  {
    ArezTestUtil.disableZones();

    final ArezContext context1 = Arez.context();
    assertNotNull( context1 );
    final ArezContext context2 = Arez.context();
    //noinspection SimplifiableAssertion
    assertTrue( context1 == context2 );
  }

  @Test
  public void zone_basicOperation()
  {
    ArezTestUtil.enableZones();

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );

    final Zone zone1 = Arez.createZone();

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );

    zone1.safeRun( () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
    } );

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
  }

  @Test
  public void zone_multipleZones()
  {
    ArezTestUtil.enableZones();

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );

    final Zone zone1 = Arez.createZone();
    final Zone zone2 = Arez.createZone();
    final Zone zone3 = Arez.createZone();

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );
    assertFalse( zone2.isActive() );
    assertFalse( zone3.isActive() );

    zone1.safeRun( () -> {

      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
      assertFalse( zone2.isActive() );
      assertFalse( zone3.isActive() );

      zone2.safeRun( () -> {

        assertEquals( zone2.getContext(), Arez.context() );
        assertEquals( ZoneHolder.getZoneStack().size(), 2 );
        assertEquals( ZoneHolder.getZoneStack().get( 0 ), ZoneHolder.getDefaultZone() );
        assertEquals( ZoneHolder.getZoneStack().get( 1 ), zone1 );
        assertFalse( zone1.isActive() );
        assertTrue( zone2.isActive() );
        assertFalse( zone3.isActive() );

        zone1.safeRun( () -> {

          assertEquals( zone1.getContext(), Arez.context() );
          assertEquals( ZoneHolder.getZoneStack().size(), 3 );
          assertEquals( ZoneHolder.getZoneStack().get( 0 ), ZoneHolder.getDefaultZone() );
          assertEquals( ZoneHolder.getZoneStack().get( 1 ), zone1 );
          assertEquals( ZoneHolder.getZoneStack().get( 2 ), zone2 );
          assertTrue( zone1.isActive() );
          assertFalse( zone2.isActive() );
          assertFalse( zone3.isActive() );

          zone3.safeRun( () -> {

            assertEquals( zone3.getContext(), Arez.context() );
            assertEquals( ZoneHolder.getZoneStack().size(), 4 );
            assertEquals( ZoneHolder.getZoneStack().get( 0 ), ZoneHolder.getDefaultZone() );
            assertEquals( ZoneHolder.getZoneStack().get( 1 ), zone1 );
            assertEquals( ZoneHolder.getZoneStack().get( 2 ), zone2 );
            assertEquals( ZoneHolder.getZoneStack().get( 3 ), zone1 );
            assertFalse( zone1.isActive() );
            assertFalse( zone2.isActive() );
            assertTrue( zone3.isActive() );

          } );

          assertEquals( zone1.getContext(), Arez.context() );
          assertEquals( ZoneHolder.getZoneStack().size(), 3 );
          assertEquals( ZoneHolder.getZoneStack().get( 0 ), ZoneHolder.getDefaultZone() );
          assertEquals( ZoneHolder.getZoneStack().get( 1 ), zone1 );
          assertEquals( ZoneHolder.getZoneStack().get( 2 ), zone2 );
          assertTrue( zone1.isActive() );
          assertFalse( zone2.isActive() );
          assertFalse( zone3.isActive() );

        } );

        assertEquals( zone2.getContext(), Arez.context() );
        assertEquals( ZoneHolder.getZoneStack().size(), 2 );
        assertEquals( ZoneHolder.getZoneStack().get( 0 ), ZoneHolder.getDefaultZone() );
        assertEquals( ZoneHolder.getZoneStack().get( 1 ), zone1 );
        assertFalse( zone1.isActive() );
        assertTrue( zone2.isActive() );
        assertFalse( zone3.isActive() );

      } );

      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ZoneHolder.getZoneStack().size(), 1 );
      assertEquals( ZoneHolder.getZoneStack().get( 0 ), ZoneHolder.getDefaultZone() );
      assertTrue( zone1.isActive() );
      assertFalse( zone2.isActive() );
      assertFalse( zone3.isActive() );

    } );

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
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

    assertInvariantFailure( () -> Arez.activateZone( new Zone( ValueUtil.randomString() ) ),
                            "Arez-0002: Invoked Arez.activateZone() but zones are not enabled." );
  }

  @Test
  public void deactivateZone_whenZonesNotEnabled()
  {
    ArezTestUtil.disableZones();

    assertInvariantFailure( () -> Arez.deactivateZone( new Zone( ValueUtil.randomString() ) ),
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
    assertInvariantFailure( () -> Arez.deactivateZone( Arez.createZone() ),
                            "Arez-0004: Attempted to deactivate zone that is not active." );
  }
}
