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

public final class ComputableValueDisposeEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final ComputableValue<?> computableValue = context.computable( "Foo@1", ValueUtil::randomString );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );
    final ComputableValueDisposeEvent event = new ComputableValueDisposeEvent( info );

    assertEquals( event.getComputableValue(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputableValueDispose" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 3 );
  }

  @Test
  public void basicOperation_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final ArezContext context = Arez.context();
    final ComputableValue<?> computableValue = context.computable( "Foo@1", ValueUtil::randomString );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );
    final ComputableValueDisposeEvent event = new ComputableValueDisposeEvent( info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ComputableValueDispose" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertNull( data.get( "zone" ) );
    assertEquals( data.size(), 2 );
  }
}
