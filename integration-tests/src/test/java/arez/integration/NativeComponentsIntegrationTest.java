package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputedValue;
import arez.Disposable;
import arez.Observable;
import arez.Observer;
import arez.integration.util.SpyEventRecorder;
import arez.spy.ComponentInfo;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NativeComponentsIntegrationTest
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

    final Observable<String> observable1 = context.createObservable( component, "Attr1", null, null );
    final Observable<String> observable2 = context.createObservable( component, "Attr2", null, null );

    final ComputedValue<String> computedValue1 =
      context.computedValue( component, "Attr3", () -> "", null, null, null, null );

    final Observer observer1 = context.tracker( component, "Render", true, () -> {
    } );

    assertFalse( component.isComplete() );

    component.complete();

    assertTrue( component.isComplete() );

    final ComponentInfo info = context.getSpy().findComponent( type, id );
    assertNotNull( info );
    assertEquals( info.getId(), id );
    assertEquals( info.getName(), component.getName() );
    assertEquals( info.getObservables().size(), 2 );
    assertEquals( info.getObservables().get( 0 ).getName(), observable1.getName() );
    assertEquals( info.getObservables().get( 1 ).getName(), observable2.getName() );
    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().iterator().next().getName(), observer1.getName() );
    assertEquals( info.getComputedValues().size(), 1 );
    assertEquals( info.getComputedValues().get( 0 ).getName(), computedValue1.getName() );

    assertEquals( context.getSpy().findAllComponentTypes().size(), 1 );
    assertEquals( context.getSpy().findAllComponentTypes().contains( type ), true );

    assertEquals( context.getSpy().findAllComponentsByType( type ).size(), 1 );
    assertEquals( context.getSpy().findAllComponentsByType( type ).iterator().next().getName(), component.getName() );

    Disposable.dispose( component );

    assertTrue( Disposable.isDisposed( component ) );
    assertTrue( Disposable.isDisposed( observable1 ) );
    assertTrue( Disposable.isDisposed( observable2 ) );
    assertTrue( Disposable.isDisposed( computedValue1 ) );
    assertTrue( Disposable.isDisposed( observer1 ) );

    assertNull( context.getSpy().findComponent( type, id ) );
    assertEquals( context.getSpy().findAllComponentsByType( type ).size(), 0 );
    assertEquals( context.getSpy().findAllComponentTypes().size(), 0 );

    assertMatchesFixture( recorder );
  }
}
