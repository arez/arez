package arez;

import java.text.ParseException;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ZoneTest
  extends AbstractTest
{
  /**
   * Verifies default and custom zone names are assigned.
   */
  @Test
  public void zone_names_defaultAndCustom()
  {
    ArezTestUtil.enableZones();

    // Default zone should have a name when names enabled
    final Zone defaultZone = ZoneHolder.getDefaultZone();
    assertNotNull( defaultZone.getName() );
    assertTrue( defaultZone.getName().startsWith( "Zone@" ) );

    // create unnamed zone should auto-generate incrementing name
    final Zone z2 = Arez.createZone();
    assertNotNull( z2.getName() );
    assertTrue( z2.getName().startsWith( "Zone@" ) );

    final Zone z3 = Arez.createZone();
    assertNotNull( z3.getName() );
    assertTrue( z3.getName().startsWith( "Zone@" ) );

    // create named zone should keep name
    final Zone zNamed = Arez.createZone( "MyZone" );
    assertEquals( zNamed.getName(), "MyZone" );
  }

  @Test
  public void zone_names_disabled()
  {
    ArezTestUtil.enableZones();
    ArezTestUtil.disableNames();

    assertInvariantFailure( () -> assertNull( ZoneHolder.getDefaultZone().getName() ),
                            "Arez-0169: Zone.getName() invoked when Arez.areNamesEnabled() is false" );
  }

  /**
   * Verifies zone activates during safe function execution.
   */
  @Test
  public void zone_safeRun_Function()
  {
    ArezTestUtil.enableZones();

    final Zone zone1 = Arez.createZone();

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );

    final String expected = ValueUtil.randomString();
    final String actual = zone1.safeRun( () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
      return expected;
    } );

    assertEquals( actual, expected );

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
  }

  @Test
  public void zone_safeRun_Procedure()
  {
    ArezTestUtil.enableZones();

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
  public void zone_run_Function_throwsException()
  {
    ArezTestUtil.enableZones();

    final Zone zone1 = Arez.createZone();

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );

    assertThrows( ParseException.class, () -> zone1.run( () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
      throw new ParseException( "", 1 );
    } ) );

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
  }

  @Test
  public void zone_run_Procedure_throwsException()
  {
    ArezTestUtil.enableZones();

    final Zone zone1 = Arez.createZone();

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );

    final Procedure procedure = () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
      throw new ParseException( "", 1 );
    };
    assertThrows( ParseException.class, () -> zone1.run( procedure ) );

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
  }

  @Test
  public void zone_run_Procedure_completesNormally()
    throws Throwable
  {
    ArezTestUtil.enableZones();

    final Zone zone1 = Arez.createZone();

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );

    final Procedure procedure = () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
    };
    zone1.run( procedure );

    assertEquals( ZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ZoneHolder.getZoneStack().size(), 0 );
  }
}
