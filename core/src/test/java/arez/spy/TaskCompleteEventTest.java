package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.Task;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TaskCompleteEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final int duration = 23;
    final ArezContext context = Arez.context();
    final Task task = context.task( ValueUtil::randomString );
    final TaskInfo info = context.getSpy().asTaskInfo( task );
    final TaskCompleteEvent event = new TaskCompleteEvent( info, null, duration );

    assertEquals( event.getTask(), info );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TaskComplete" );
    assertEquals( data.get( "name" ), task.getName() );
    assertEquals( data.get( "duration" ), duration );
    assertNull( data.get( "errorMessage" ) );
    assertEquals( data.size(), 4 );
  }

  @Test
  public void basicOperation_withError()
  {
    final int duration = 42;
    final Throwable throwable = new Throwable( "Boo!" );
    final ArezContext context = Arez.context();
    final Task task = context.task( ValueUtil::randomString );
    final TaskInfo info = context.getSpy().asTaskInfo( task );
    final TaskCompleteEvent event = new TaskCompleteEvent( info, throwable, duration );

    assertEquals( event.getTask(), info );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TaskComplete" );
    assertEquals( data.get( "name" ), task.getName() );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.get( "errorMessage" ), "Boo!" );
    assertEquals( data.size(), 4 );
  }
}
