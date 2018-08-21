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
    final long duration = 44L;
    final ComputeCompletedEvent event = new ComputeCompletedEvent( info, duration );

    assertEquals( event.getComputedValue(), info );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputeCompleted" );
    assertEquals( data.get( "computed" ), "Foo@1" );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.size(), 3 );
  }
}
