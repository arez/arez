package arez.spy;

import arez.AbstractTest;
import arez.ArezTestUtil;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ReactionCycleStartEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ReactionCycleStartEvent event = new ReactionCycleStartEvent();

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ReactionCycleStart" );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 2 );
  }

  @Test
  public void basicOperation_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final ReactionCycleStartEvent event = new ReactionCycleStartEvent();

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ReactionCycleStart" );
    assertNull( data.get( "zone" ) );
    assertEquals( data.size(), 1 );
  }
}
