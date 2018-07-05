package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComponentCreateCompletedEvent;
import arez.spy.ComponentDisposeCompletedEvent;
import arez.spy.ComponentDisposeStartedEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import java.util.concurrent.atomic.AtomicReference;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentTest
  extends AbstractArezTest
{
  @Test
  public void constructorPassedContext_whenZonesDisabled()
  {
    ArezTestUtil.disableZones();
    ArezTestUtil.resetState();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> new Component( Arez.context(),
                                         ValueUtil.randomString(),
                                         ValueUtil.randomString(),
                                         ValueUtil.randomString(),
                                         null,
                                         null ) );

    assertEquals( exception.getMessage(),
                  "Arez-0175: Component passed a context but Arez.areZonesEnabled() is false" );
  }

  @Test
  public void basicOperation()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();
    final String name = ValueUtil.randomString();

    final Component component = new Component( context, type, id, name, null, null );
    assertEquals( component.getContext(), context );
    assertEquals( component.getType(), type );
    assertEquals( component.getId(), id );
    assertEquals( component.getName(), name );
    assertEquals( component.toString(), name );
  }

  @Test
  public void basicOperationIntegerId()
    throws Exception
  {
    final Integer id = ValueUtil.randomInt();
    final Component component =
      new Component( Arez.context(), ValueUtil.randomString(), id, ValueUtil.randomString(), null, null );
    assertEquals( component.getId(), id );
  }

  @Test
  public void noNameSuppliedWhenNamesDisabled()
    throws Exception
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();
    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();

    final Component component = new Component( context, type, id, null, null, null );
    assertEquals( component.getContext(), context );
    assertEquals( component.getType(), type );
    assertEquals( component.getId(), id );

    assertTrue( component.toString().startsWith( "arez.Component@" ) );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, component::getName );
    assertEquals( exception.getMessage(),
                  "Arez-0038: Component.getName() invoked when Arez.areNamesEnabled() is false" );
  }

  @Test
  public void nameSuppliedWhenNamesDisabled()
    throws Exception
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();
    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();
    final String name = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> new Component( context, type, id, name, null, null ) );

    assertEquals( exception.getMessage(),
                  "Arez-0037: Component passed a name '" + name + "' but Arez.areNamesEnabled() is false" );
  }

  @Test
  public void complete()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component = context.component( ValueUtil.randomString(), ValueUtil.randomString(), name );

    final Observable observable1 = context.observable();
    final ComputedValue computedValue1 = context.computedValue( () -> "" );
    final Observer observer1 = context.autorun( AbstractArezTest::observeADependency );

    component.addObservable( observable1 );
    component.addComputedValue( computedValue1 );
    component.addObserver( observer1 );

    assertEquals( component.isComplete(), false );

    component.complete();

    assertEquals( component.isComplete(), true );

    assertEquals( component.getObservables().size(), 1 );
    assertEquals( component.getComputedValues().size(), 1 );
    assertEquals( component.getObservers().size(), 1 );
  }

  @Test
  public void complete_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component = context.component( ValueUtil.randomString(), ValueUtil.randomString(), name );

    assertEquals( component.isComplete(), false );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    component.complete();

    handler.assertEventCount( 1 );

    final ComponentCreateCompletedEvent event = handler.assertNextEvent( ComponentCreateCompletedEvent.class );
    assertEquals( event.getComponent(), component );
  }

  @Test
  public void observers()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final Observer observer1 = context.autorun( AbstractArezTest::observeADependency );
    final Observer observer2 = context.autorun( AbstractArezTest::observeADependency );

    assertEquals( component.getObservers().size(), 0 );

    component.addObserver( observer1 );

    assertEquals( component.getObservers().size(), 1 );
    assertEquals( component.getObservers().contains( observer1 ), true );

    component.addObserver( observer2 );

    assertEquals( component.getObservers().size(), 2 );
    assertEquals( component.getObservers().contains( observer1 ), true );
    assertEquals( component.getObservers().contains( observer2 ), true );

    component.removeObserver( observer1 );

    assertEquals( component.getObservers().size(), 1 );
    assertEquals( component.getObservers().contains( observer2 ), true );

    component.removeObserver( observer2 );

    assertEquals( component.getObservers().size(), 0 );
  }

  @Test
  public void addObserver_duplicate()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final Observer observer1 = context.autorun( AbstractArezTest::observeADependency );

    component.addObserver( observer1 );

    assertEquals( component.getObservers().size(), 1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> component.addObserver( observer1 ) );
    assertEquals( exception.getMessage(), "Arez-0040: Component.addObserver invoked on component '" + name +
                                          "' specifying observer named '" + observer1.getName() +
                                          "' when observer already exists for component." );

    assertEquals( component.getObservers().size(), 1 );
  }

  @Test
  public void addObserver_complete()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );
    component.complete();

    final Observer observer1 = context.autorun( AbstractArezTest::observeADependency );

    assertEquals( component.getObservers().size(), 0 );

    // We should be able to add observers after the component is complete. This is typically used
    // for when observers that we want associated with the component. (i.e. The watchers contained
    // by repositories that remove entry in repository when entity is disposed)
    component.addObserver( observer1 );

    assertEquals( component.getObservers().size(), 1 );
    assertEquals( component.getObservers().contains( observer1 ), true );
  }

  @Test
  public void removeObserver_noExist()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final Observer observer1 = context.autorun( AbstractArezTest::observeADependency );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> component.removeObserver( observer1 ) );
    assertEquals( exception.getMessage(), "Arez-0041: Component.removeObserver invoked on component '" + name +
                                          "' specifying observer named '" + observer1.getName() +
                                          "' when observer does not exist for component." );
  }

  @Test
  public void observables()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final Observable observable1 = context.observable();
    final Observable observable2 = context.observable();

    assertEquals( component.getObservables().size(), 0 );

    component.addObservable( observable1 );

    assertEquals( component.getObservables().size(), 1 );
    assertEquals( component.getObservables().contains( observable1 ), true );

    component.addObservable( observable2 );

    assertEquals( component.getObservables().size(), 2 );
    assertEquals( component.getObservables().contains( observable1 ), true );
    assertEquals( component.getObservables().contains( observable2 ), true );

    component.removeObservable( observable1 );

    assertEquals( component.getObservables().size(), 1 );
    assertEquals( component.getObservables().contains( observable2 ), true );

    component.removeObservable( observable2 );

    assertEquals( component.getObservables().size(), 0 );
  }

  @Test
  public void addObservable_duplicate()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final Observable observable1 = context.observable();

    component.addObservable( observable1 );

    assertEquals( component.getObservables().size(), 1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> component.addObservable( observable1 ) );
    assertEquals( exception.getMessage(), "Arez-0043: Component.addObservable invoked on component '" + name +
                                          "' specifying observable named '" + observable1.getName() +
                                          "' when observable already exists for component." );

    assertEquals( component.getObservables().size(), 1 );
  }

  @Test
  public void addObservable_complete()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );
    component.complete();

    final Observable observable1 = context.observable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> component.addObservable( observable1 ) );
    assertEquals( exception.getMessage(), "Arez-0042: Component.addObservable invoked on component '" + name +
                                          "' specifying observable named '" + observable1.getName() +
                                          "' when component.complete() has already been called." );
  }

  @Test
  public void removeObservable_noExist()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final Observable observable1 = context.observable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> component.removeObservable( observable1 ) );
    assertEquals( exception.getMessage(), "Arez-0044: Component.removeObservable invoked on component '" + name +
                                          "' specifying observable named '" + observable1.getName() +
                                          "' when observable does not exist for component." );
  }

  @Test
  public void computedValues()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ComputedValue computedValue1 = context.computedValue( () -> "" );
    final ComputedValue computedValue2 = context.computedValue( () -> "" );

    assertEquals( component.getComputedValues().size(), 0 );

    component.addComputedValue( computedValue1 );

    assertEquals( component.getComputedValues().size(), 1 );
    assertEquals( component.getComputedValues().contains( computedValue1 ), true );

    component.addComputedValue( computedValue2 );

    assertEquals( component.getComputedValues().size(), 2 );
    assertEquals( component.getComputedValues().contains( computedValue1 ), true );
    assertEquals( component.getComputedValues().contains( computedValue2 ), true );

    component.removeComputedValue( computedValue1 );

    assertEquals( component.getComputedValues().size(), 1 );
    assertEquals( component.getComputedValues().contains( computedValue2 ), true );

    component.removeComputedValue( computedValue2 );

    assertEquals( component.getComputedValues().size(), 0 );
  }

  @Test
  public void addComputedValue_duplicate()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ComputedValue computedValue1 = context.computedValue( () -> "" );

    component.addComputedValue( computedValue1 );

    assertEquals( component.getComputedValues().size(), 1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> component.addComputedValue( computedValue1 ) );
    assertEquals( exception.getMessage(), "Arez-0046: Component.addComputedValue invoked on component '" + name +
                                          "' specifying computedValue named '" + computedValue1.getName() +
                                          "' when computedValue already exists for component." );

    assertEquals( component.getComputedValues().size(), 1 );
  }

  @Test
  public void removeComputedValue_noExist()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ComputedValue computedValue1 = context.computedValue( () -> "" );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> component.removeComputedValue( computedValue1 ) );
    assertEquals( exception.getMessage(), "Arez-0047: Component.removeComputedValue invoked on component '" + name +
                                          "' specifying computedValue named '" + computedValue1.getName() +
                                          "' when computedValue does not exist for component." );
  }

  @Test
  public void dispose()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component = context.component( ValueUtil.randomString(), ValueUtil.randomString(), name );

    final Observable observable1 = context.observable( component, ValueUtil.randomString(), null, null );
    final Observable observable2 = context.observable( component, ValueUtil.randomString(), null, null );
    final ComputedValue computedValue1 = context.computedValue( component,
                                                                ValueUtil.randomString(),
                                                                () -> "",
                                                                null,
                                                                null,
                                                                null,
                                                                null );
    final ComputedValue computedValue2 = context.computedValue( component,
                                                                ValueUtil.randomString(),
                                                                () -> "",
                                                                null,
                                                                null,
                                                                null,
                                                                null );
    final Procedure action = AbstractArezTest::observeADependency;
    final Observer observer1 =
      context.autorun( component, ValueUtil.randomString(), true, action, Priority.NORMAL, false );
    final Observer observer2 =
      context.autorun( component, ValueUtil.randomString(), true, action, Priority.NORMAL, false );

    assertEquals( component.getObservables().size(), 2 );
    assertEquals( component.getComputedValues().size(), 2 );
    assertEquals( component.getObservers().size(), 2 );

    assertFalse( Disposable.isDisposed( component ) );
    assertFalse( Disposable.isDisposed( observable1 ) );
    assertFalse( Disposable.isDisposed( observable2 ) );
    assertFalse( Disposable.isDisposed( observer1 ) );
    assertFalse( Disposable.isDisposed( observer2 ) );
    assertFalse( Disposable.isDisposed( computedValue1 ) );
    assertFalse( Disposable.isDisposed( computedValue2 ) );

    assertTrue( context.isComponentPresent( component.getType(), component.getId() ) );

    component.dispose();

    assertFalse( context.isComponentPresent( component.getType(), component.getId() ) );

    assertEquals( component.getObservables().size(), 0 );
    assertEquals( component.getComputedValues().size(), 0 );
    assertEquals( component.getObservers().size(), 0 );

    assertTrue( Disposable.isDisposed( component ) );
    assertTrue( Disposable.isDisposed( observable1 ) );
    assertTrue( Disposable.isDisposed( observable2 ) );
    assertTrue( Disposable.isDisposed( observer1 ) );
    assertTrue( Disposable.isDisposed( observer2 ) );
    assertTrue( Disposable.isDisposed( computedValue1 ) );
    assertTrue( Disposable.isDisposed( computedValue2 ) );
  }

  @Test
  public void dispose_pre_and_post_hooks()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final AtomicReference<Observable> observable = new AtomicReference<>();

    final SafeProcedure preDispose = () -> assertFalse( Disposable.isDisposed( observable.get() ) );
    final SafeProcedure postDispose = () -> assertTrue( Disposable.isDisposed( observable.get() ) );
    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), name, preDispose, postDispose );

    final Observable observable1 = context.observable( component, ValueUtil.randomString(), null, null );

    observable.set( observable1 );

    component.complete();

    assertEquals( component.getObservables().size(), 1 );

    assertFalse( Disposable.isDisposed( component ) );
    assertFalse( Disposable.isDisposed( observable1 ) );

    assertTrue( context.isComponentPresent( component.getType(), component.getId() ) );

    component.dispose();

    assertFalse( context.isComponentPresent( component.getType(), component.getId() ) );

    assertEquals( component.getObservables().size(), 0 );

    assertTrue( Disposable.isDisposed( component ) );
    assertTrue( Disposable.isDisposed( observable1 ) );
  }

  @Test
  public void dispose_spyEventsGenerated()
  {
    final ArezContext context = Arez.context();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    component.dispose();

    handler.assertEventCount( 6 );
    {
      final ComponentDisposeStartedEvent event = handler.assertNextEvent( ComponentDisposeStartedEvent.class );
      assertEquals( event.getComponent(), component );
    }

    final String actionName = component.getName() + ".dispose";
    {
      final ActionStartedEvent event = handler.assertNextEvent( ActionStartedEvent.class );
      assertEquals( event.getName(), actionName );
    }
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    {
      final ActionCompletedEvent event = handler.assertNextEvent( ActionCompletedEvent.class );
      assertEquals( event.getName(), actionName );
    }

    {
      final ComponentDisposeCompletedEvent event = handler.assertNextEvent( ComponentDisposeCompletedEvent.class );
      assertEquals( event.getComponent(), component );
    }
  }
}
