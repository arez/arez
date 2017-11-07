package org.realityforge.arez.integration;

import java.util.Objects;
import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Component;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.Observable;
import org.realityforge.arez.Observer;
import org.realityforge.arez.spy.ComponentInfo;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NativeComponentsIntegrationTest
  extends AbstractIntegrationTest
{
  @Test
  public void inspectComponentViaSpy()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );

    final String type = "MyType";
    final String id = "1";

    assertFalse( context.isComponentPresent( type, id ) );

    final Component component = context.createComponent( type, id );

    assertTrue( context.isComponentPresent( type, id ) );

    final Observable<String> observable1 = context.createObservable( component, "Attr1", null, null );
    final Observable<String> observable2 = context.createObservable( component, "Attr2", null, null );

    final ComputedValue<String> computedValue1 =
      context.createComputedValue( component, "Attr3", () -> "", Objects::equals, null, null, null, null );

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
    assertEquals( info.getObservables().contains( observable1 ), true );
    assertEquals( info.getObservables().contains( observable2 ), true );
    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().contains( observer1 ), true );
    assertEquals( info.getComputedValues().size(), 1 );
    assertEquals( info.getComputedValues().contains( computedValue1 ), true );

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

    assertEqualsFixture( recorder.eventsAsString() );
  }
}
