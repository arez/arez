package arez.spy;

import arez.AbstractTest;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "ConstantConditions" )
public final class TransactionCompleteEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final String name = "Foo@1";
    final boolean mutation = true;
    final ObserverInfo tracker = null;
    final int duration = 23;
    final TransactionCompleteEvent event = new TransactionCompleteEvent( name, mutation, tracker, duration );

    assertEquals( event.getName(), name );
    assertEquals( event.isMutation(), mutation );
    assertEquals( event.getTracker(), tracker );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TransactionComplete" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "mutation" ), mutation );
    assertEquals( data.get( "tracker" ), tracker );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.size(), 5 );
  }
}
