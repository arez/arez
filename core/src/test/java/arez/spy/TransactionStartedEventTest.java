package arez.spy;

import arez.AbstractArezTest;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TransactionStartedEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final String name = "Foo@1";
    final boolean mutation = true;
    final ObserverInfo tracker = null;
    final TransactionStartedEvent event = new TransactionStartedEvent( name, mutation, tracker );

    assertEquals( event.getName(), name );
    assertEquals( event.isMutation(), mutation );
    assertEquals( event.getTracker(), tracker );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TransactionStarted" );
    assertEquals( data.get( "transaction" ), name );
    assertEquals( data.get( "mutation" ), mutation );
    assertEquals( data.get( "tracker" ), tracker );
    assertEquals( data.size(), 4 );
  }
}
