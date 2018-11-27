package arez.spy;

import arez.AbstractArezTest;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TaskCompleteEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final String name = "Foo@1";
    final int duration = 23;
    final TaskCompleteEvent event = new TaskCompleteEvent( name, null, duration );

    assertEquals( event.getName(), name );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TaskComplete" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "duration" ), duration );
    assertNull( data.get( "errorMessage" ) );
    assertEquals( data.size(), 4 );
  }

  @Test
  public void basicOperation_withError()
  {
    final String name = "Foo@1";
    final int duration = 42;
    final Throwable throwable = new Throwable( "Boo!" );
    final TaskCompleteEvent event = new TaskCompleteEvent( name, throwable, duration );

    assertEquals( event.getName(), name );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TaskComplete" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.get( "errorMessage" ), "Boo!" );
    assertEquals( data.size(), 4 );
  }
}
