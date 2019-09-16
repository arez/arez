package arez.spy;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.ObservableValue;
import java.util.HashMap;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableValueCreateEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<?> observableValue = context.observable( "Foo@1" );
    final ObservableValueInfo info = context.getSpy().asObservableValueInfo( observableValue );
    final ObservableValueCreateEvent event = new ObservableValueCreateEvent( info );

    assertEquals( event.getObservableValue(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ObservableValueCreate" );
    assertEquals( data.get( "name" ), "Foo@1" );
    assertEquals( data.size(), 2 );
  }
}
