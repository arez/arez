package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.Component;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentDisposeCompleteEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final Component component = context.component( "Foo", "1" );
    final ComponentInfo info = context.getSpy().asComponentInfo( component );
    final ComponentDisposeCompleteEvent event = new ComponentDisposeCompleteEvent( info );

    assertEquals( event.getComponentInfo(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComponentDisposeComplete" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertEquals( data.size(), 2 );
  }
}
