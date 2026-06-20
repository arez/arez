package arez.spy;

import arez.AbstractTest;
import arez.ArezTestUtil;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ReactionCycleCompleteEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final int duration = 23;
    final ReactionCycleCompleteEvent event = new ReactionCycleCompleteEvent( null, duration );

    assertEquals( event.getDuration(), duration );
    assertNull( event.getThrowable() );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ReactionCycleComplete" );
    assertEquals( data.get( "duration" ), duration );
    assertNull( data.get( "errorMessage" ) );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 4 );
  }

  @Test
  public void basicOperation_withError()
  {
    final int duration = 42;
    final Throwable throwable = new Throwable( "Boo!" );
    final ReactionCycleCompleteEvent event = new ReactionCycleCompleteEvent( throwable, duration );

    assertEquals( event.getDuration(), duration );
    assertEquals( event.getThrowable(), throwable );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ReactionCycleComplete" );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.get( "errorMessage" ), "Boo!" );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 4 );
  }

  @Test
  public void basicOperation_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final int duration = 23;
    final ReactionCycleCompleteEvent event = new ReactionCycleCompleteEvent( null, duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ReactionCycleComplete" );
    assertEquals( data.get( "duration" ), duration );
    assertNull( data.get( "errorMessage" ) );
    assertNull( data.get( "zone" ) );
    assertEquals( data.size(), 3 );
  }
}
