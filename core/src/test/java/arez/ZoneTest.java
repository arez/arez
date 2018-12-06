package arez;

import java.text.ParseException;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ZoneTest
  extends AbstractArezTest
{
  @Test
  public void zone_safeRun_Function()
  {
    ArezTestUtil.enableZones();

    final Zone zone1 = Arez.createZone();

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );

    final String expected = ValueUtil.randomString();
    final String actual = zone1.safeRun( () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ArezZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
      return expected;
    } );

    assertEquals( actual, expected );

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
  }

  @Test
  public void zone_safeRun_Procedure()
  {
    ArezTestUtil.enableZones();

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
  public void zone_run_Function_throwsException()
  {
    ArezTestUtil.enableZones();

    final Zone zone1 = Arez.createZone();

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );

    assertThrows( ParseException.class, () -> zone1.run( () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ArezZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
      throw new ParseException( "", 1 );
    } ) );

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
  }

  @Test
  public void zone_run_Procedure_throwsException()
  {
    ArezTestUtil.enableZones();

    final Zone zone1 = Arez.createZone();

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );

    final Procedure procedure = () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ArezZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
      throw new ParseException( "", 1 );
    };
    assertThrows( ParseException.class, () -> zone1.run( procedure ) );

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
  }

  @Test
  public void zone_run_Procedure_completesNormally()
    throws Throwable
  {
    ArezTestUtil.enableZones();

    final Zone zone1 = Arez.createZone();

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
    assertFalse( zone1.isActive() );

    final Procedure procedure = () -> {
      assertEquals( zone1.getContext(), Arez.context() );
      assertEquals( ArezZoneHolder.getZoneStack().size(), 1 );
      assertTrue( zone1.isActive() );
    };
    zone1.run( procedure );

    assertEquals( ArezZoneHolder.getDefaultZone().getContext(), Arez.context() );
    assertEquals( ArezZoneHolder.getZoneStack().size(), 0 );
  }
}
