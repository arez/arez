package arez.spy;

import arez.AbstractTest;
import arez.ArezTestUtil;
import arez.Arez;
import arez.ArezContext;
import arez.Observer;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ObserveStartEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final String name = "Foo@1";
    final Observer observer = context.tracker( name, ValueUtil::randomString );
    final ObserverInfo info = context.getSpy().asObserverInfo( observer );
    final ObserveStartEvent event = new ObserveStartEvent( info );

    assertEquals( event.getObserver(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ObserveStart" );
    assertEquals( data.get( "name" ), name );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 3 );
  }

  @Test
  public void basicOperation_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final ArezContext context = Arez.context();
    final String name = "Foo@1";
    final Observer observer = context.tracker( name, ValueUtil::randomString );
    final ObserverInfo info = context.getSpy().asObserverInfo( observer );
    final ObserveStartEvent event = new ObserveStartEvent( info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ObserveStart" );
    assertEquals( data.get( "name" ), name );
    assertNull( data.get( "zone" ) );
    assertEquals( data.size(), 2 );
  }
}
