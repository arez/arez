package arez.spy;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComputeStartEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final ComputableValue<?> computableValue = context.computable( "Foo@1", ValueUtil::randomString );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );
    final ComputeStartEvent event = new ComputeStartEvent( info );

    assertEquals( event.getComputableValue(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputeStart" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertEquals( data.size(), 2 );
  }
}
