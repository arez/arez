package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputableValueCreateEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final ComputableValue<?> computableValue = context.computable( "Foo@1", ValueUtil::randomString );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );
    final ComputableValueCreateEvent event = new ComputableValueCreateEvent( info );

    assertEquals( event.getComputableValue(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputableValueCreate" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertEquals( data.size(), 2 );
  }
}
