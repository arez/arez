package arez.spy;

import arez.AbstractTest;
import arez.ArezTestUtil;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "ConstantConditions" )
public final class ActionCompleteEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final String name = ValueUtil.randomString();
    final boolean tracked = true;
    final Object[] parameters = new Object[ 0 ];
    final boolean returnsResult = true;
    final Object result = new Object();
    final Throwable throwable = null;
    final int duration = 33;
    final ActionCompleteEvent event =
      new ActionCompleteEvent( name, tracked, parameters, returnsResult, result, throwable, duration );

    assertEquals( event.getName(), name );
    assertEquals( event.isTracked(), tracked );
    assertEquals( event.getParameters(), parameters );
    assertEquals( event.returnsResult(), returnsResult );
    assertEquals( event.getResult(), result );
    assertEquals( event.getThrowable(), throwable );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ActionComplete" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "tracked" ), tracked );
    assertEquals( data.get( "parameters" ), parameters );
    assertEquals( data.get( "duration" ), duration );
    assertNull( data.get( "errorMessage" ) );
    assertEquals( data.get( "returnsResult" ), true );
    assertEquals( data.get( "result" ), result );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 9 );
  }

  @Test
  public void basicOperation_exception()
  {
    final String name = ValueUtil.randomString();
    final boolean tracked = true;
    final Object[] parameters = new Object[ 0 ];
    final boolean returnsResult = true;
    final Object result = null;
    final Throwable throwable = new Exception( "X" );
    final int duration = 33;
    final ActionCompleteEvent event =
      new ActionCompleteEvent( name, tracked, parameters, returnsResult, result, throwable, duration );

    assertEquals( event.getName(), name );
    assertEquals( event.isTracked(), tracked );
    assertEquals( event.getParameters(), parameters );
    assertEquals( event.returnsResult(), returnsResult );
    assertEquals( event.getResult(), result );
    assertEquals( event.getThrowable(), throwable );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ActionComplete" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "tracked" ), tracked );
    assertEquals( data.get( "parameters" ), parameters );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.get( "errorMessage" ), "X" );
    assertEquals( data.get( "returnsResult" ), true );
    assertNull( data.get( "result" ) );
    assertNotNull( data.get( "zone" ) );
    assertEquals( data.size(), 9 );
  }

  @Test
  public void basicOperation_zonesDisabled()
  {
    ArezTestUtil.disableZones();

    final String name = ValueUtil.randomString();
    final boolean tracked = true;
    final Object[] parameters = new Object[ 0 ];
    final boolean returnsResult = true;
    final Object result = new Object();
    final int duration = 33;
    final ActionCompleteEvent event =
      new ActionCompleteEvent( name, tracked, parameters, returnsResult, result, null, duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ActionComplete" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "tracked" ), tracked );
    assertEquals( data.get( "parameters" ), parameters );
    assertEquals( data.get( "duration" ), duration );
    assertNull( data.get( "errorMessage" ) );
    assertEquals( data.get( "returnsResult" ), true );
    assertEquals( data.get( "result" ), result );
    assertNull( data.get( "zone" ) );
    assertEquals( data.size(), 8 );
  }
}
