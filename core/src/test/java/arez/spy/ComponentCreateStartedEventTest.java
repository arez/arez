package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.Component;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentCreateStartedEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final Component component = Arez.context().component( "Foo", "1" );
    final ComponentCreateStartedEvent event = new ComponentCreateStartedEvent( component );

    assertEquals( event.getComponent(), component );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComponentCreateStarted" );
    assertEquals( data.get( "component" ), "Foo@1" );
    assertEquals( data.size(), 2 );
  }
}
