package arez;

import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ComponentCreateCompleteEvent;
import arez.spy.ComponentDisposeCompleteEvent;
import arez.spy.ComponentDisposeStartEvent;
import arez.spy.ComponentInfo;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import java.util.concurrent.atomic.AtomicReference;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComponentTest
  extends AbstractTest
{
  @Test
  public void constructorPassedContext_whenZonesDisabled()
  {
    ArezTestUtil.disableZones();

    assertInvariantFailure( () -> new Component( Arez.context(),
                                                 ValueUtil.randomString(),
                                                 ValueUtil.randomString(),
                                                 ValueUtil.randomString(),
                                                 null,
                                                 null ),
                            "Arez-0175: Component passed a context but Arez.areZonesEnabled() is false" );
  }

  @Test
  public void basicOperation()
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
  {
    final Integer id = ValueUtil.randomInt();
    final Component component =
      new Component( Arez.context(), ValueUtil.randomString(), id, ValueUtil.randomString(), null, null );
    assertEquals( component.getId(), id );
  }

  @Test
  public void noNameSuppliedWhenNamesDisabled()
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

    assertInvariantFailure( component::getName,
                            "Arez-0038: Component.getName() invoked when Arez.areNamesEnabled() is false" );
  }

  @Test
  public void nameSuppliedWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final ArezContext context = Arez.context();
    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();
    final String name = ValueUtil.randomString();

    assertInvariantFailure( () -> new Component( context, type, id, name, null, null ),
                            "Arez-0037: Component passed a name '" + name + "' but Arez.areNamesEnabled() is false" );
  }

  @Test
  public void complete()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component = context.component( ValueUtil.randomString(), ValueUtil.randomString(), name );

    final ObservableValue<?> observableValue1 = context.observable();
    final ComputableValue<?> computableValue1 = context.computable( () -> "" );
    final Observer observer1 = context.observer( AbstractTest::observeADependency );

    component.addObservableValue( observableValue1 );
    component.addComputableValue( computableValue1 );
    component.addObserver( observer1 );

    assertFalse( component.isComplete() );

    component.complete();

    assertTrue( component.isComplete() );

    assertEquals( component.getObservableValues().size(), 1 );
    assertEquals( component.getComputableValues().size(), 1 );
    assertEquals( component.getObservers().size(), 1 );
  }

  @Test
  public void complete_generates_spyEvent()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component = context.component( ValueUtil.randomString(), ValueUtil.randomString(), name );

    assertFalse( component.isComplete() );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    component.complete();

    handler.assertEventCount( 1 );

    handler.assertNextEvent( ComponentCreateCompleteEvent.class,
                             event -> assertEquals( event.getComponentInfo().getName(), component.getName() ) );
  }

  @Test
  public void observers()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final Observer observer1 = context.observer( AbstractTest::observeADependency );
    final Observer observer2 = context.observer( AbstractTest::observeADependency );

    assertEquals( component.getObservers().size(), 0 );

    component.addObserver( observer1 );

    assertEquals( component.getObservers().size(), 1 );
    assertTrue( component.getObservers().contains( observer1 ) );

    component.addObserver( observer2 );

    assertEquals( component.getObservers().size(), 2 );
    assertTrue( component.getObservers().contains( observer1 ) );
    assertTrue( component.getObservers().contains( observer2 ) );

    component.removeObserver( observer1 );

    assertEquals( component.getObservers().size(), 1 );
    assertTrue( component.getObservers().contains( observer2 ) );

    component.removeObserver( observer2 );

    assertEquals( component.getObservers().size(), 0 );
  }

  @Test
  public void addObserver_duplicate()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final Observer observer1 = context.observer( AbstractTest::observeADependency );

    component.addObserver( observer1 );

    assertEquals( component.getObservers().size(), 1 );

    assertInvariantFailure( () -> component.addObserver( observer1 ),
                            "Arez-0040: Component.addObserver invoked on component '" + name +
                            "' specifying observer named '" + observer1.getName() +
                            "' when observer already exists for component." );

    assertEquals( component.getObservers().size(), 1 );
  }

  @Test
  public void addObserver_complete()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );
    component.complete();

    final Observer observer1 = context.observer( AbstractTest::observeADependency );

    assertEquals( component.getObservers().size(), 0 );

    // We should be able to add observers after the component is complete. This is typically used
    // for when observers that we want associated with the component. (i.e. The watchers contained
    // by repositories that remove entry in repository when entity is disposed)
    component.addObserver( observer1 );

    assertEquals( component.getObservers().size(), 1 );
    assertTrue( component.getObservers().contains( observer1 ) );
  }

  @Test
  public void removeObserver_noExist()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final Observer observer1 = context.observer( AbstractTest::observeADependency );

    assertInvariantFailure( () -> component.removeObserver( observer1 ),
                            "Arez-0041: Component.removeObserver invoked on component '" + name +
                            "' specifying observer named '" + observer1.getName() +
                            "' when observer does not exist for component." );
  }

  @Test
  public void observables()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ObservableValue<?> observableValue1 = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();

    assertEquals( component.getObservableValues().size(), 0 );

    component.addObservableValue( observableValue1 );

    assertEquals( component.getObservableValues().size(), 1 );
    assertTrue( component.getObservableValues().contains( observableValue1 ) );

    component.addObservableValue( observableValue2 );

    assertEquals( component.getObservableValues().size(), 2 );
    assertTrue( component.getObservableValues().contains( observableValue1 ) );
    assertTrue( component.getObservableValues().contains( observableValue2 ) );

    component.removeObservableValue( observableValue1 );

    assertEquals( component.getObservableValues().size(), 1 );
    assertTrue( component.getObservableValues().contains( observableValue2 ) );

    component.removeObservableValue( observableValue2 );

    assertEquals( component.getObservableValues().size(), 0 );
  }

  @Test
  public void addObservable_duplicate()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ObservableValue<?> observableValue1 = context.observable();

    component.addObservableValue( observableValue1 );

    assertEquals( component.getObservableValues().size(), 1 );

    assertInvariantFailure( () -> component.addObservableValue( observableValue1 ),
                            "Arez-0043: Component.addObservableValue invoked on component '" + name +
                            "' specifying ObservableValue named '" + observableValue1.getName() +
                            "' when ObservableValue already exists for component." );

    assertEquals( component.getObservableValues().size(), 1 );
  }

  @Test
  public void addObservable_complete()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );
    component.complete();

    final ObservableValue<?> observableValue1 = context.observable();

    assertInvariantFailure( () -> component.addObservableValue( observableValue1 ),
                            "Arez-0042: Component.addObservableValue invoked on component '" + name +
                            "' specifying ObservableValue named '" + observableValue1.getName() +
                            "' when component.complete() has already been called." );
  }

  @Test
  public void removeObservable_noExist()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ObservableValue<?> observableValue1 = context.observable();

    assertInvariantFailure( () -> component.removeObservableValue( observableValue1 ),
                            "Arez-0044: Component.removeObservableValue invoked on component '" + name +
                            "' specifying ObservableValue named '" + observableValue1.getName() +
                            "' when ObservableValue does not exist for component." );
  }

  @Test
  public void computableValues()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ComputableValue<?> computableValue1 = context.computable( () -> "" );
    final ComputableValue<?> computableValue2 = context.computable( () -> "" );

    assertEquals( component.getComputableValues().size(), 0 );

    component.addComputableValue( computableValue1 );

    assertEquals( component.getComputableValues().size(), 1 );
    assertTrue( component.getComputableValues().contains( computableValue1 ) );

    component.addComputableValue( computableValue2 );

    assertEquals( component.getComputableValues().size(), 2 );
    assertTrue( component.getComputableValues().contains( computableValue1 ) );
    assertTrue( component.getComputableValues().contains( computableValue2 ) );

    component.removeComputableValue( computableValue1 );

    assertEquals( component.getComputableValues().size(), 1 );
    assertTrue( component.getComputableValues().contains( computableValue2 ) );

    component.removeComputableValue( computableValue2 );

    assertEquals( component.getComputableValues().size(), 0 );
  }

  @Test
  public void addComputableValue_duplicate()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ComputableValue<?> computableValue1 = context.computable( () -> "" );

    component.addComputableValue( computableValue1 );

    assertEquals( component.getComputableValues().size(), 1 );

    assertInvariantFailure( () -> component.addComputableValue( computableValue1 ),
                            "Arez-0046: Component.addComputableValue invoked on component '" + name +
                            "' specifying ComputableValue named '" + computableValue1.getName() +
                            "' when ComputableValue already exists for component." );

    assertEquals( component.getComputableValues().size(), 1 );
  }

  @Test
  public void removeComputableValue_noExist()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component =
      new Component( context, ValueUtil.randomString(), ValueUtil.randomString(), name, null, null );

    final ComputableValue<?> computableValue1 = context.computable( () -> "" );

    assertInvariantFailure( () -> component.removeComputableValue( computableValue1 ),
                            "Arez-0047: Component.removeComputableValue invoked on component '" + name +
                            "' specifying ComputableValue named '" + computableValue1.getName() +
                            "' when ComputableValue does not exist for component." );
  }

  @Test
  public void dispose()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final Component component = context.component( ValueUtil.randomString(), ValueUtil.randomString(), name );

    final ObservableValue<?> observableValue1 = context.observable( component, ValueUtil.randomString(), null, null );
    final ObservableValue<?> observableValue2 = context.observable( component, ValueUtil.randomString(), null, null );
    final ComputableValue<?> computableValue1 = context.computable( component, ValueUtil.randomString(), () -> "" );
    final ComputableValue<?> computableValue2 = context.computable( component, ValueUtil.randomString(), () -> "" );
    final Procedure action = AbstractTest::observeADependency;
    final Observer observer1 = context.observer( component, null, action, Observer.Flags.RUN_LATER );
    final Observer observer2 = context.observer( component, null, action, Observer.Flags.RUN_LATER );

    assertEquals( component.getObservableValues().size(), 2 );
    assertEquals( component.getComputableValues().size(), 2 );
    assertEquals( component.getObservers().size(), 2 );

    assertFalse( Disposable.isDisposed( component ) );
    assertFalse( Disposable.isDisposed( observableValue1 ) );
    assertFalse( Disposable.isDisposed( observableValue2 ) );
    assertFalse( Disposable.isDisposed( observer1 ) );
    assertFalse( Disposable.isDisposed( observer2 ) );
    assertFalse( Disposable.isDisposed( computableValue1 ) );
    assertFalse( Disposable.isDisposed( computableValue2 ) );

    assertTrue( context.isComponentPresent( component.getType(), component.getId() ) );

    component.dispose();

    assertFalse( context.isComponentPresent( component.getType(), component.getId() ) );

    assertEquals( component.getObservableValues().size(), 0 );
    assertEquals( component.getComputableValues().size(), 0 );
    assertEquals( component.getObservers().size(), 0 );

    assertTrue( Disposable.isDisposed( component ) );
    assertTrue( Disposable.isDisposed( observableValue1 ) );
    assertTrue( Disposable.isDisposed( observableValue2 ) );
    assertTrue( Disposable.isDisposed( observer1 ) );
    assertTrue( Disposable.isDisposed( observer2 ) );
    assertTrue( Disposable.isDisposed( computableValue1 ) );
    assertTrue( Disposable.isDisposed( computableValue2 ) );
  }

  @Test
  public void dispose_pre_and_post_hooks()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();

    final AtomicReference<ObservableValue<?>> observable = new AtomicReference<>();

    final SafeProcedure preDispose = () -> assertFalse( Disposable.isDisposed( observable.get() ) );
    final SafeProcedure postDispose = () -> assertTrue( Disposable.isDisposed( observable.get() ) );
    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), name, preDispose, postDispose );

    final ObservableValue<?> observableValue1 = context.observable( component, ValueUtil.randomString(), null, null );

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

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    component.dispose();

    handler.assertEventCount( 6 );
    handler.assertNextEvent( ComponentDisposeStartEvent.class,
                             e -> assertEquals( e.getComponentInfo().getName(), component.getName() ) );

    final String actionName = component.getName() + ".dispose";
    handler.assertNextEvent( ActionStartEvent.class, e -> assertEquals( e.getName(), actionName ) );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> assertEquals( e.getName(), actionName ) );
    handler.assertNextEvent( ComponentDisposeCompleteEvent.class,
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

    final ArezContext context = Arez.context();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );

    assertInvariantFailure( component::asInfo,
                            "Arez-0194: Component.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
  }
}
