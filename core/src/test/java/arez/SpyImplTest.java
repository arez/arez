package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputableValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import arez.spy.Spy;
import arez.spy.SpyEventHandler;
import arez.spy.TaskInfo;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SpyImplTest
  extends AbstractTest
{
  @Test
  public void constructorPassedContext_whenZonesDisabled()
  {
    ArezTestUtil.disableZones();

    assertInvariantFailure( () -> new SpyImpl( Arez.context() ),
                            "Arez-185: SpyImpl passed a context but Arez.areZonesEnabled() is false" );
  }

  @Test
  public void basicOperation()
  {
    final SpyImpl spy = (SpyImpl) Arez.context().getSpy();

    final Object event = new Object();

    final AtomicInteger callCount = new AtomicInteger();

    final SpyEventHandler handler = e -> {
      callCount.incrementAndGet();
      assertEquals( e, event );
    };

    assertFalse( spy.willPropagateSpyEvents() );

    spy.addSpyEventHandler( handler );

    assertTrue( spy.willPropagateSpyEvents() );

    assertEquals( callCount.get(), 0 );

    spy.reportSpyEvent( event );

    assertEquals( callCount.get(), 1 );

    spy.removeSpyEventHandler( handler );

    assertFalse( spy.willPropagateSpyEvents() );

    assertInvariantFailure( () -> spy.reportSpyEvent( event ),
                            "Arez-0104: Attempting to report SpyEvent '" + event +
                            "' but willPropagateSpyEvents() returns false." );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void isTransactionActive()
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    assertFalse( spy.isTransactionActive() );

    setupReadOnlyTransaction( context );

    assertTrue( spy.isTransactionActive() );
  }

  @Test
  public void getTransaction()
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final String name = ValueUtil.randomString();
    context.safeAction( name, () -> {
      observeADependency();
      assertEquals( spy.getTransaction().getName(), name );
    } );
  }

  @Test
  public void getTransaction_whenNoTransaction()
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    assertInvariantFailure( spy::getTransaction, "Arez-0105: Spy.getTransaction() invoked but no transaction active." );
  }

  @Test
  public void component_finders()
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final String type = ValueUtil.randomString();
    final String id1 = ValueUtil.randomString();
    final String id2 = ValueUtil.randomString();

    assertEquals( spy.findAllComponentTypes().size(), 0 );
    assertEquals( spy.findAllComponentsByType( type ).size(), 0 );

    final Component component = context.component( type, id1, ValueUtil.randomString() );

    assertEquals( spy.findAllComponentTypes().size(), 1 );
    assertTrue( spy.findAllComponentTypes().contains( type ) );

    assertEquals( spy.findAllComponentsByType( ValueUtil.randomString() ).size(), 0 );

    final Collection<ComponentInfo> componentsByType1 = spy.findAllComponentsByType( type );
    assertEquals( componentsByType1.size(), 1 );
    assertTrue( componentsByType1.stream().anyMatch( c -> c.getName().equals( component.getName() ) ) );

    final Component component2 = context.component( type, id2, ValueUtil.randomString() );

    assertEquals( spy.findAllComponentTypes().size(), 1 );
    assertTrue( spy.findAllComponentTypes().contains( type ) );
    assertUnmodifiable( spy.findAllComponentTypes() );

    assertEquals( spy.findAllComponentsByType( ValueUtil.randomString() ).size(), 0 );

    final Collection<ComponentInfo> componentsByType2 = spy.findAllComponentsByType( type );
    assertUnmodifiable( componentsByType2 );
    assertEquals( componentsByType2.size(), 2 );
    assertTrue( componentsByType2.stream().anyMatch( c -> c.getName().equals( component.getName() ) ) );
    assertTrue( componentsByType2.stream().anyMatch( c -> c.getName().equals( component2.getName() ) ) );

    final ComponentInfo info1 = spy.findComponent( type, id1 );
    final ComponentInfo info2 = spy.findComponent( type, id2 );
    assertNotNull( info1 );
    assertNotNull( info2 );
    assertEquals( info1.getName(), component.getName() );
    assertEquals( info2.getName(), component2.getName() );
    assertNull( spy.findComponent( type, ValueUtil.randomString() ) );
    assertNull( spy.findComponent( ValueUtil.randomString(), id2 ) );
  }

  @Test
  public void findComponent_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();

    assertInvariantFailure( () -> spy.findComponent( type, id ),
                            "Arez-0010: ArezContext.findComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllComponentsByType_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final String type = ValueUtil.randomString();

    assertInvariantFailure( () -> spy.findAllComponentsByType( type ),
                            "Arez-0011: ArezContext.findAllComponentsByType() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllComponentTypes_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    assertInvariantFailure( spy::findAllComponentTypes,
                            "Arez-0012: ArezContext.findAllComponentTypes() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllTopLevelTasks()
  {
    final ArezContext context = Arez.context();

    final Task task = context.task( ValueUtil::randomString );

    final Spy spy = context.getSpy();

    final Collection<TaskInfo> values = spy.findAllTopLevelTasks();
    assertEquals( values.size(), 1 );
    assertEquals( values.iterator().next().getName(), task.getName() );
    assertUnmodifiable( values );
  }

  @Test
  public void findAllTopLevelTasks_registriesDisabled()
  {
    ArezTestUtil.disableRegistries();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    assertInvariantFailure( spy::findAllTopLevelTasks,
                            "Arez-0228: ArezContext.getTopLevelTasks() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void findAllTopLevelObservables()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<String> observableValue = context.observable();

    final Spy spy = context.getSpy();

    final Collection<ObservableValueInfo> values = spy.findAllTopLevelObservableValues();
    assertEquals( values.size(), 1 );
    assertEquals( values.iterator().next().getName(), observableValue.getName() );
    assertUnmodifiable( values );
  }

  @Test
  public void findAllTopLevelObservables_registriesDisabled()
  {
    ArezTestUtil.disableRegistries();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    assertInvariantFailure( spy::findAllTopLevelObservableValues,
                            "Arez-0026: ArezContext.getTopLevelObservables() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void findAllTopLevelComputableValues()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );

    final Spy spy = context.getSpy();

    final Collection<ComputableValueInfo> values = spy.findAllTopLevelComputableValues();
    assertEquals( values.size(), 1 );
    assertEquals( values.iterator().next().getName(), computableValue.getName() );
    assertUnmodifiable( values );

    assertEquals( spy.findAllTopLevelObservers().size(), 0 );
    assertEquals( spy.findAllTopLevelObservableValues().size(), 0 );
  }

  @Test
  public void findAllTopLevelComputableValues_registriesDisabled()
  {
    ArezTestUtil.disableRegistries();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    assertInvariantFailure( spy::findAllTopLevelComputableValues,
                            "Arez-0036: ArezContext.getTopLevelComputableValues() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void findAllTopLevelObservers()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( AbstractTest::observeADependency );

    final Spy spy = context.getSpy();

    final Collection<ObserverInfo> values = spy.findAllTopLevelObservers();
    assertEquals( values.size(), 1 );
    assertEquals( values.iterator().next().getName(), observer.getName() );
    assertUnmodifiable( values );
  }

  @Test
  public void findAllTopLevelObservers_registriesDisabled()
  {
    ArezTestUtil.disableRegistries();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    assertInvariantFailure( spy::findAllTopLevelObservers,
                            "Arez-0031: ArezContext.getTopLevelObservers() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void observable_getValue_noAccessor()
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ObservableValue<Integer> observableValue = context.observable();

    assertInvariantFailure( () -> context.action( () -> spy.asObservableValueInfo( observableValue ).getValue() ),
                            "Arez-0112: Spy.getValue invoked on ObservableValue named '" + observableValue.getName() +
                            "' but ObservableValue has no property accessor." );
  }

  @Test
  public void asComponentInfo()
  {
    final ArezContext context = Arez.context();
    final Component component = context.component( ValueUtil.randomString(), ValueUtil.randomString() );
    final ComponentInfo info = context.getSpy().asComponentInfo( component );

    assertEquals( info.getName(), component.getName() );
  }

  @Test
  public void asObserverInfo()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.observer( AbstractTest::observeADependency );
    final ObserverInfo info = context.getSpy().asObserverInfo( observer );

    assertEquals( info.getName(), observer.getName() );
  }

  @Test
  public void asTaskInfo()
  {
    final ArezContext context = Arez.context();
    final Task task = context.task( ValueUtil::randomString );
    final TaskInfo info = context.getSpy().asTaskInfo( task );

    assertEquals( info.getName(), task.getName() );
  }

  @Test
  public void asObservableInfo()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observableValue = context.observable( ValueUtil.randomString() );
    final ObservableValueInfo info = context.getSpy().asObservableValueInfo( observableValue );

    assertEquals( info.getName(), observableValue.getName() );
  }

  @Test
  public void asComputableValueInfo()
  {
    final ArezContext context = Arez.context();
    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final ComputableValueInfo info = context.getSpy().asComputableValueInfo( computableValue );

    assertEquals( info.getName(), computableValue.getName() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> list )
  {
    assertThrows( UnsupportedOperationException.class, () -> list.remove( list.iterator().next() ) );
  }
}
