package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComponentCreateCompletedEvent;
import arez.spy.ComponentDisposeCompletedEvent;
import arez.spy.ComponentDisposeStartedEvent;
import arez.spy.ComponentInfo;
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

    final ObservableValue observableValue1 = context.observable();
    final ComputedValue computedValue1 = context.computed( () -> "" );
    final Observer observer1 = context.observer( AbstractArezTest::observeADependency );

    component.addObservableValue( observableValue1 );
    component.addComputedValue( computedValue1 );
    component.addObserver( observer1 );

    assertEquals( component.isComplete(), false );

    component.complete();

    assertEquals( component.isComplete(), true );

    assertEquals( component.getObservableValues().size(), 1 );
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

    handler.assertNextEvent( ComponentCreateCompletedEvent.class,
                             event -> assertEquals( event.getComponentInfo().getName(), component.getName() ) );
  }

  @Test
  public void observers()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final Observer observer1 = context.observer( AbstractArezTest::observeADependency );
    final Observer observer2 = context.observer( AbstractArezTest::observeADependency );

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

    final Observer observer1 = context.observer( AbstractArezTest::observeADependency );

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

    final Observer observer1 = context.observer( AbstractArezTest::observeADependency );

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

    final Observer observer1 = context.observer( AbstractArezTest::observeADependency );

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

    final ObservableValue observableValue1 = context.observable();
    final ObservableValue observableValue2 = context.observable();

    assertEquals( component.getObservableValues().size(), 0 );

    component.addObservableValue( observableValue1 );

    assertEquals( component.getObservableValues().size(), 1 );
    assertEquals( component.getObservableValues().contains( observableValue1 ), true );

    component.addObservableValue( observableValue2 );

    assertEquals( component.getObservableValues().size(), 2 );
    assertEquals( component.getObservableValues().contains( observableValue1 ), true );
    assertEquals( component.getObservableValues().contains( observableValue2 ), true );

    component.removeObservableValue( observableValue1 );

    assertEquals( component.getObservableValues().size(), 1 );
    assertEquals( component.getObservableValues().contains( observableValue2 ), true );

    component.removeObservableValue( observableValue2 );

    assertEquals( component.getObservableValues().size(), 0 );
  }

  @Test
  public void addObservable_duplicate()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ObservableValue observableValue1 = context.observable();

    component.addObservableValue( observableValue1 );

    assertEquals( component.getObservableValues().size(), 1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> component.addObservableValue( observableValue1 ) );
    assertEquals( exception.getMessage(), "Arez-0043: Component.addObservableValue invoked on component '" + name +
                                          "' specifying ObservableValue named '" + observableValue1.getName() +
                                          "' when ObservableValue already exists for component." );

    assertEquals( component.getObservableValues().size(), 1 );
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

    final ObservableValue observableValue1 = context.observable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> component.addObservableValue( observableValue1 ) );
    assertEquals( exception.getMessage(), "Arez-0042: Component.addObservableValue invoked on component '" + name +
                                          "' specifying ObservableValue named '" + observableValue1.getName() +
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

    final ObservableValue observableValue1 = context.observable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> component.removeObservableValue( observableValue1 ) );
    assertEquals( exception.getMessage(), "Arez-0044: Component.removeObservableValue invoked on component '" + name +
                                          "' specifying ObservableValue named '" + observableValue1.getName() +
                                          "' when ObservableValue does not exist for component." );
  }

  @Test
  public void computedValues()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ComputedValue computedValue1 = context.computed( () -> "" );
    final ComputedValue computedValue2 = context.computed( () -> "" );

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

    final ComputedValue computedValue1 = context.computed( () -> "" );

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

    final ComputedValue computedValue1 = context.computed( () -> "" );

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

    final ObservableValue observableValue1 = context.observable( component, ValueUtil.randomString(), null, null );
    final ObservableValue observableValue2 = context.observable( component, ValueUtil.randomString(), null, null );
    final ComputedValue computedValue1 = context.computed( component,
                                                           ValueUtil.randomString(),
                                                           () -> "",
                                                           null,
                                                           null,
                                                           null );
    final ComputedValue computedValue2 = context.computed( component,
                                                           ValueUtil.randomString(),
                                                           () -> "",
                                                           null,
                                                           null,
                                                           null );
    final Procedure action = AbstractArezTest::observeADependency;
    final Observer observer1 = context.observer( component, null, action, Flags.RUN_LATER );
    final Observer observer2 = context.observer( component, null, action, Flags.RUN_LATER );

    assertEquals( component.getObservableValues().size(), 2 );
    assertEquals( component.getComputedValues().size(), 2 );
    assertEquals( component.getObservers().size(), 2 );

    assertFalse( Disposable.isDisposed( component ) );
    assertFalse( Disposable.isDisposed( observableValue1 ) );
    assertFalse( Disposable.isDisposed( observableValue2 ) );
    assertFalse( Disposable.isDisposed( observer1 ) );
    assertFalse( Disposable.isDisposed( observer2 ) );
    assertFalse( Disposable.isDisposed( computedValue1 ) );
    assertFalse( Disposable.isDisposed( computedValue2 ) );

    assertTrue( context.isComponentPresent( component.getType(), component.getId() ) );

    component.dispose();

    assertFalse( context.isComponentPresent( component.getType(), component.getId() ) );

    assertEquals( component.getObservableValues().size(), 0 );
    assertEquals( component.getComputedValues().size(), 0 );
    assertEquals( component.getObservers().size(), 0 );

    assertTrue( Disposable.isDisposed( component ) );
    assertTrue( Disposable.isDisposed( observableValue1 ) );
    assertTrue( Disposable.isDisposed( observableValue2 ) );
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

    final AtomicReference<ObservableValue> observable = new AtomicReference<>();

    final SafeProcedure preDispose = () -> assertFalse( Disposable.isDisposed( observable.get() ) );
    final SafeProcedure postDispose = () -> assertTrue( Disposable.isDisposed( observable.get() ) );
    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), name, preDispose, postDispose );

    final ObservableValue observableValue1 = context.observable( component, ValueUtil.randomString(), null, null );

    observable.set( observableValue1 );

    component.complete();

    assertEquals( component.getObservableValues().size(), 1 );

    assertFalse( Disposable.isDisposed( component ) );
    assertFalse( Disposable.isDisposed( observableValue1 ) );

    assertTrue( context.isComponentPresent( component.getType(), component.getId() ) );

    component.dispose();

    assertFalse( context.isComponentPresent( component.getType(), component.getId() ) );

    assertEquals( component.getObservableValues().size(), 0 );

    assertTrue( Disposable.isDisposed( component ) );
    assertTrue( Disposable.isDisposed( observableValue1 ) );
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
    handler.assertNextEvent( ComponentDisposeStartedEvent.class,
                             e -> assertEquals( e.getComponentInfo().getName(), component.getName() ) );

    final String actionName = component.getName() + ".dispose";
    handler.assertNextEvent( ActionStartedEvent.class, e -> assertEquals( e.getName(), actionName ) );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> assertEquals( e.getName(), actionName ) );
    handler.assertNextEvent( ComponentDisposeCompletedEvent.class,
                             e -> assertEquals( e.getComponentInfo().getName(), component.getName() ) );
  }

  @Test
  public void asInfo()
  {
    final ArezContext context = Arez.context();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final ComponentInfo info = component.asInfo();
    assertEquals( info.getId(), component.getId() );
    assertEquals( info.getName(), component.getName() );
    assertEquals( info.getType(), component.getType() );
  }

  @Test
  public void asInfo_spyDisabled()
  {
    ArezTestUtil.disableSpies();
    ArezTestUtil.resetState();

    final ArezContext context = Arez.context();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, component::asInfo );
    assertEquals( exception.getMessage(),
                  "Arez-0194: Component.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
  }
}
