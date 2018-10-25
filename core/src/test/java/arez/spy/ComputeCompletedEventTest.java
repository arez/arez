package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputeCompletedEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final ComputedValue<?> computedValue = context.computed( "Foo@1", ValueUtil::randomString );
    final ComputedValueInfo info = context.getSpy().asComputedValueInfo( computedValue );
    final Object result = new Object();
    final int duration = 44;
    final ComputeCompletedEvent event = new ComputeCompletedEvent( info, result, null, duration );

    assertEquals( event.getComputedValue(), info );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputeCompleted" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertEquals( data.get( "result" ), result );
    assertNull( data.get( "errorMessage" ) );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.size(), 5 );
  }

  @Test
  public void basicOperation_withError()
  {
    final ArezContext context = Arez.context();
    final ComputedValue<?> computedValue = context.computed( "Foo@1", ValueUtil::randomString );
    final ComputedValueInfo info = context.getSpy().asComputedValueInfo( computedValue );
    final int duration = 44;
    final ComputeCompletedEvent event = new ComputeCompletedEvent( info, null, new Error( "I am an error" ), duration );

    assertEquals( event.getComputedValue(), info );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputeCompleted" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertNull( data.get( "result" ) );
    assertEquals( data.get( "errorMessage" ), "I am an error" );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.size(), 5 );
  }
}
