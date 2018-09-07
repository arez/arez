package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.Observer;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ReactionCompletedEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final String name = "Foo@1";
    final Observer observer = context.tracker( name, ValueUtil::randomString );
    final ObserverInfo info = context.getSpy().asObserverInfo( observer );
    final long duration = 23L;
    final ReactionCompletedEvent event = new ReactionCompletedEvent( info, duration );

    assertEquals( event.getObserver(), info );
    assertEquals( event.getDuration(), duration );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ReactionCompleted" );
    assertEquals( data.get( "observer" ), name );
    assertEquals( data.get( "duration" ), duration );
    assertEquals( data.size(), 3 );
  }
}
