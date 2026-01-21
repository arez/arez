package arez.spy;

import arez.AbstractTest;
import arez.ArezTestUtil;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ActionSkippedEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final String name = ValueUtil.randomString();
    final boolean tracked = true;
    final Object[] parameters = new Object[ 0 ];
    final ActionSkippedEvent event = new ActionSkippedEvent( name, tracked, parameters );

    assertEquals( event.getName(), name );
    assertEquals( event.isTracked(), tracked );
    assertEquals( event.getParameters(), parameters );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ActionSkipped" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "tracked" ), tracked );
    assertEquals( data.get( "parameters" ), parameters );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 5 );
  }

  @Test
  public void basicOperation_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final String name = ValueUtil.randomString();
    final boolean tracked = true;
    final Object[] parameters = new Object[ 0 ];
    final ActionSkippedEvent event = new ActionSkippedEvent( name, tracked, parameters );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ActionSkipped" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "tracked" ), tracked );
    assertEquals( data.get( "parameters" ), parameters );
    assertNull( data.get( "zone" ) );
    assertEquals( data.size(), 4 );
  }
}
