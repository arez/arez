package arez.spy;

import arez.AbstractTest;
import arez.ArezTestUtil;
import arez.Arez;
import arez.ArezContext;
import arez.Component;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComponentDisposeStartEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final Component component = context.component( "Foo", "1" );
    final ComponentInfo info = context.getSpy().asComponentInfo( component );
    final ComponentDisposeStartEvent event = new ComponentDisposeStartEvent( info );

    assertEquals( event.getComponentInfo(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComponentDisposeStart" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 3 );
  }

  @Test
  public void basicOperation_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final ArezContext context = Arez.context();
    final Component component = context.component( "Foo", "1" );
    final ComponentInfo info = context.getSpy().asComponentInfo( component );
    final ComponentDisposeStartEvent event = new ComponentDisposeStartEvent( info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComponentDisposeStart" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertNull( data.get( "zone" ) );
    assertEquals( data.size(), 2 );
  }
}
