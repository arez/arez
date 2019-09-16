package arez.spy;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.Task;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TaskStartEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final Task task = context.task( ValueUtil::randomString );
    final TaskInfo info = context.getSpy().asTaskInfo( task );
    final TaskStartEvent event = new TaskStartEvent( info );

    assertEquals( event.getTask(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "TaskStart" );
    assertEquals( data.get( "name" ), info.getName() );
    assertEquals( data.size(), 2 );
  }
}
