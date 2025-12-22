package arez.spy;

import arez.AbstractTest;
import arez.ArezTestUtil;
import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComputeCompleteEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final ComputableValue<?> computableValue = context.computable( "Foo@1", ValueUtil::randomString );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );
    final Object result = new Object();
    final int duration = 44;
    final ComputeCompleteEvent event = new ComputeCompleteEvent( info, result, null, duration );

    assertEquals( event.getComputableValue(), info );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputeComplete" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertEquals( data.get( "result" ), result );
    assertNull( data.get( "errorMessage" ) );
    assertEquals( data.get( "duration" ), duration );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 6 );
  }

  @Test
  public void basicOperation_withError()
  {
    final ArezContext context = Arez.context();
    final ComputableValue<?> computableValue = context.computable( "Foo@1", ValueUtil::randomString );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );
    final int duration = 44;
    final ComputeCompleteEvent event = new ComputeCompleteEvent( info, null, new Error( "I am an error" ), duration );

    assertEquals( event.getComputableValue(), info );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputeComplete" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertNull( data.get( "result" ) );
    assertEquals( data.get( "errorMessage" ), "I am an error" );
    assertEquals( data.get( "duration" ), duration );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 6 );
  }

  @Test
  public void basicOperation_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final ArezContext context = Arez.context();
    final ComputableValue<?> computableValue = context.computable( "Foo@1", ValueUtil::randomString );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );
    final Object result = new Object();
    final int duration = 44;
    final ComputeCompleteEvent event = new ComputeCompleteEvent( info, result, null, duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputeComplete" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertEquals( data.get( "result" ), result );
    assertNull( data.get( "errorMessage" ) );
    assertEquals( data.get( "duration" ), duration );
    assertNull( data.get( "zone" ) );
    assertEquals( data.size(), 5 );
  }
}
