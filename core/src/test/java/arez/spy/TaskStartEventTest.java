package arez.spy;

import arez.AbstractArezTest;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TaskStartEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final String name = "Foo@1";
    final TaskStartEvent event = new TaskStartEvent( name );

    assertEquals( event.getName(), name );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TaskStart" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.size(), 2 );
  }
}
