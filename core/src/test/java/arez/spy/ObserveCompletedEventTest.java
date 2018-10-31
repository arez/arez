package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.Observer;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserveCompletedEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final String name = "Foo@1";
    final Observer observer = context.tracker( name, ValueUtil::randomString );
    final ObserverInfo info = context.getSpy().asObserverInfo( observer );
    final int duration = 23;
    final ObserveCompletedEvent event = new ObserveCompletedEvent( info, null, duration );

    assertEquals( event.getObserver(), info );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ObserveCompleted" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "duration" ), duration );
    assertNull( data.get( "errorMessage" ) );
    assertEquals( data.size(), 4 );
  }

  @Test
  public void basicOperation_withError()
  {
    final ArezContext context = Arez.context();
    final String name = "Foo@1";
    final Observer observer = context.tracker( name, ValueUtil::randomString );
    final ObserverInfo info = context.getSpy().asObserverInfo( observer );
    final int duration = 42;
    final Throwable throwable = new Throwable( "Boo!" );
    final ObserveCompletedEvent event = new ObserveCompletedEvent( info, throwable, duration );

    assertEquals( event.getObserver(), info );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ObserveCompleted" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.get( "errorMessage" ), "Boo!" );
    assertEquals( data.size(), 4 );
  }
}
