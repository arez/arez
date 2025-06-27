package arez.spy;

import arez.AbstractTest;
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
    assertEquals( data.size(), 4 );
  }
}
