package arez.spy;

import arez.AbstractTest;
import arez.ArezTestUtil;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "ConstantConditions" )
public final class TransactionStartEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final String name = "Foo@1";
    final boolean mutation = true;
    final ObserverInfo tracker = null;
    final TransactionStartEvent event = new TransactionStartEvent( name, mutation, tracker );

    assertEquals( event.getName(), name );
    assertEquals( event.isMutation(), mutation );
    assertEquals( event.getTracker(), tracker );

    final Map<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TransactionStart" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "mutation" ), mutation );
    assertEquals( data.get( "tracker" ), tracker );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 5 );
  }

  @Test
  public void basicOperation_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final String name = "Foo@1";
    final boolean mutation = true;
    final ObserverInfo tracker = null;
    final TransactionStartEvent event = new TransactionStartEvent( name, mutation, tracker );

    final Map<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TransactionStart" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "mutation" ), mutation );
    assertEquals( data.get( "tracker" ), tracker );
    assertNull( data.get( "zone" ) );
    assertEquals( data.size(), 4 );
  }
}
