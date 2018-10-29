package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
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
    final ComputableValue<?> computableValue = context.computable( "Foo@1", ValueUtil::randomString );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );
    final Object result = new Object();
    final int duration = 44;
    final ComputeCompletedEvent event = new ComputeCompletedEvent( info, result, null, duration );

    assertEquals( event.getComputableValue(), info );
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
    final ComputableValue<?> computableValue = context.computable( "Foo@1", ValueUtil::randomString );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );
    final int duration = 44;
    final ComputeCompletedEvent event = new ComputeCompletedEvent( info, null, new Error( "I am an error" ), duration );

    assertEquals( event.getComputableValue(), info );
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
