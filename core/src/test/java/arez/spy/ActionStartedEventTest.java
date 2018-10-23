package arez.spy;

import arez.AbstractArezTest;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ActionStartedEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final String name = ValueUtil.randomString();
    final boolean tracked = true;
    final Object[] parameters = new Object[ 0 ];
    final ActionStartedEvent event = new ActionStartedEvent( name, tracked, parameters );

    assertEquals( event.getName(), name );
    assertEquals( event.isTracked(), tracked );
    assertEquals( event.getParameters(), parameters );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ActionStarted" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "tracked" ), tracked );
    assertEquals( data.get( "parameters" ), parameters );
    assertEquals( data.size(), 4 );
  }
}
