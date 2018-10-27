package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputableValueCreatedEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final ComputableValue<?> computableValue = context.computed( "Foo@1", ValueUtil::randomString );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );
    final ComputableValueCreatedEvent event = new ComputableValueCreatedEvent( info );

    assertEquals( event.getComputableValue(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputableValueCreated" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertEquals( data.size(), 2 );
  }
}
