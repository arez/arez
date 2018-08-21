package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputeStartedEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final ComputedValue<?> computedValue = context.computed( "Foo@1", ValueUtil::randomString );
    final ComputedValueInfo info = context.getSpy().asComputedValueInfo( computedValue );
    final ComputeStartedEvent event = new ComputeStartedEvent( info );

    assertEquals( event.getComputedValue(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputeStarted" );
    assertEquals( data.get( "computed" ), "Foo@1" );
    assertEquals( data.size(), 2 );
  }
}
