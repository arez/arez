package arez.spy;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ObservableValue;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableValueChangedEventTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final String name = "Foo@1";
    final ObservableValue<?> observableValue = context.observable( name );
    final ObservableValueInfo info = context.getSpy().asObservableValueInfo( observableValue );
    final String value = ValueUtil.randomString();
    final ObservableValueChangedEvent event = new ObservableValueChangedEvent( info, value );

    assertEquals( event.getObservableValue(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ObservableValueChanged" );
    assertEquals( data.get( "observable" ), name );
    assertEquals( data.get( "value" ), value );
    assertEquals( data.size(), 3 );
  }
}
