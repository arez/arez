package arez.spy;

import arez.AbstractArezTest;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TransactionCompletedEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final String name = "Foo@1";
    final boolean mutation = true;
    final ObserverInfo tracker = null;
    final long duration = 23L;
    final TransactionCompletedEvent event = new TransactionCompletedEvent( name, mutation, tracker, duration );

    assertEquals( event.getName(), name );
    assertEquals( event.isMutation(), mutation );
    assertEquals( event.getTracker(), tracker );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TransactionCompleted" );
    assertEquals( data.get( "transaction" ), name );
    assertEquals( data.get( "mutation" ), mutation );
    assertEquals( data.get( "tracker" ), tracker );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.size(), 5 );
  }
}
