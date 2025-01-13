package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputableValue;
import arez.Disposable;
import arez.ObservableValue;
import arez.Observer;
import arez.integration.util.SpyEventRecorder;
import arez.spy.ComponentInfo;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class NativeComponentsIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void inspectComponentViaSpy()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final String type = "MyType";
    final String id = "1";

    assertFalse( context.isComponentPresent( type, id ) );

    final Component component = context.component( type, id );

    assertTrue( context.isComponentPresent( type, id ) );

    final ObservableValue<String> observableValue1 = context.observable( component, "Attr1", null, null );
    final ObservableValue<String> observableValue2 = context.observable( component, "Attr2", null, null );

    final ComputableValue<String> computableValue1 =
      context.computable( component, "Attr3", () -> "", null );

    final Observer observer1 = context.tracker( component, "Render", () -> {
    } );

    assertFalse( component.isComplete() );

    component.complete();

    assertTrue( component.isComplete() );

    final ComponentInfo info = context.getSpy().findComponent( type, id );
    assertNotNull( info );
    assertEquals( info.getId(), id );
    assertEquals( info.getName(), component.getName() );
    assertEquals( info.getObservableValues().size(), 2 );
    assertEquals( info.getObservableValues().get( 0 ).getName(), observableValue1.getName() );
    assertEquals( info.getObservableValues().get( 1 ).getName(), observableValue2.getName() );
    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().iterator().next().getName(), observer1.getName() );
    assertEquals( info.getComputableValues().size(), 1 );
    assertEquals( info.getComputableValues().get( 0 ).getName(), computableValue1.getName() );

    assertEquals( context.getSpy().findAllComponentTypes().size(), 1 );
    assertTrue( context.getSpy().findAllComponentTypes().contains( type ) );

    assertEquals( context.getSpy().findAllComponentsByType( type ).size(), 1 );
    assertEquals( context.getSpy().findAllComponentsByType( type ).iterator().next().getName(), component.getName() );

    Disposable.dispose( component );

    assertTrue( Disposable.isDisposed( component ) );
    assertTrue( Disposable.isDisposed( observableValue1 ) );
    assertTrue( Disposable.isDisposed( observableValue2 ) );
    assertTrue( Disposable.isDisposed( computableValue1 ) );
    assertTrue( Disposable.isDisposed( observer1 ) );

    assertNull( context.getSpy().findComponent( type, id ) );
    assertEquals( context.getSpy().findAllComponentsByType( type ).size(), 0 );
    assertEquals( context.getSpy().findAllComponentTypes().size(), 0 );

    assertMatchesFixture( recorder );
  }
}
