package arez;

import arez.spy.Priority;
import arez.spy.TaskInfo;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TaskInfoImplTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final Task task = context.task( name, ValueUtil::randomString );

    final TaskInfo info = task.asInfo();

    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertTrue( info.isIdle() );
    assertEquals( info.getPriority(), Priority.NORMAL );
    assertFalse( info.isScheduled() );
    assertFalse( info.isDisposed() );

    task.dispose();

    assertTrue( info.isDisposed() );
    assertFalse( info.isScheduled() );
    assertFalse( info.isIdle() );
  }

  @Test
  public void isIdleAndScheduled()
  {
    final ArezContext context = Arez.context();

    final String name = ValueUtil.randomString();
    final Task task = context.task( name, ValueUtil::randomString );

    final TaskInfo info = task.asInfo();

    assertTrue( info.isIdle() );
    assertFalse( info.isScheduled() );

    task.markAsQueued();

    assertFalse( info.isIdle() );
    assertTrue( info.isScheduled() );

    task.markAsIdle();

    assertTrue( info.isIdle() );
    assertFalse( info.isScheduled() );
  }

  @Test
  public void equalsAndHashCode()
  {
    final ArezContext context = Arez.context();
    final Task task1 = context.task( ValueUtil::randomString );
    final Task task2 = context.task( ValueUtil::randomString );

    final TaskInfo info1a = task1.asInfo();
    final TaskInfo info1b = new TaskInfoImpl( task1 );
    final TaskInfo info2 = task2.asInfo();

    assertNotEquals( "", info1a );

    assertEquals( info1a, info1a );
    assertEquals( info1a, info1b );
    assertNotEquals( info1a, info2 );

    assertEquals( info1b, info1a );
    assertEquals( info1b, info1b );
    assertNotEquals( info1b, info2 );

    assertNotEquals( info2, info1a );
    assertNotEquals( info2, info1b );
    assertEquals( info2, info2 );

    assertEquals( info1a.hashCode(), task1.hashCode() );
    assertEquals( info1a.hashCode(), info1b.hashCode() );
    assertEquals( info2.hashCode(), task2.hashCode() );
  }
}
