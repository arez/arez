package arez.spy;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.ObservableValue;
import java.util.HashMap;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableValueChangeEventTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final String name = "Foo@1";
    final ObservableValue<?> observableValue = context.observable( name );
    final ObservableValueInfo info = context.getSpy().asObservableValueInfo( observableValue );
    final String value = ValueUtil.randomString();
    final ObservableValueChangeEvent event = new ObservableValueChangeEvent( info, value );

    assertEquals( event.getObservableValue(), info );

    final HashMap<String, Object> data = new HashMap<>();
    event.toMap( data );

    assertEquals( data.get( "type" ), "ObservableValueChange" );
    assertEquals( data.get( "name" ), name );
    assertEquals( data.get( "value" ), value );
    assertEquals( data.size(), 3 );
  }
}
