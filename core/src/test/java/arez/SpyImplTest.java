package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputedValueInfo;
import arez.spy.ElementInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SpyImplTest
  extends AbstractArezTest
{
  @Test
  public void constructorPassedContext_whenZonesDisabled()
  {
    ArezTestUtil.disableZones();
    ArezTestUtil.resetState();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> new SpyImpl( Arez.context() ) );

    assertEquals( exception.getMessage(),
                  "Arez-185: SpyImpl passed a context but Arez.areZonesEnabled() is false" );
  }

  @Test
  public void basicOperation()
    throws Exception
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

    assertEquals( spy.getSpyEventHandlers().size(), 1 );
    assertEquals( spy.getSpyEventHandlers().contains( handler ), true );

    assertEquals( callCount.get(), 0 );

    spy.reportSpyEvent( event );

    assertEquals( callCount.get(), 1 );

    spy.removeSpyEventHandler( handler );

    assertFalse( spy.willPropagateSpyEvents() );

    assertEquals( spy.getSpyEventHandlers().size(), 0 );
  }

  @Test
  public void reportSpyEvent_whenNoListeners()
  {
    final Spy spy = Arez.context().getSpy();

    assertFalse( spy.willPropagateSpyEvents() );

    final Object event = new Object();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.reportSpyEvent( event ) );

    assertEquals( exception.getMessage(),
                  "Arez-0104: Attempting to report SpyEvent '" + event +
                  "' but willPropagateSpyEvents() returns false." );
  }

  @Test
  public void addSpyEventHandler_alreadyExists()
    throws Exception
  {
    final Spy spy = Arez.context().getSpy();

    final SpyEventHandler handler = new TestSpyEventHandler();
    spy.addSpyEventHandler( handler );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.addSpyEventHandler( handler ) );

    assertEquals( exception.getMessage(),
                  "Arez-0102: Attempting to add handler " + handler + " that is already in the list of spy handlers." );
  }

  @Test
  public void removeSpyEventHandler_noExists()
    throws Exception
  {
    final Spy spy = Arez.context().getSpy();

    final SpyEventHandler handler = new TestSpyEventHandler();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.removeSpyEventHandler( handler ) );

    assertEquals( exception.getMessage(),
                  "Arez-0103: Attempting to remove handler " + handler + " that is not in the list of spy handlers." );
  }

  @Test
  public void multipleHandlers()
  {
    final SpyImpl spy = (SpyImpl) Arez.context().getSpy();

    final Object event = new Object();

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final SpyEventHandler handler1 = e -> callCount1.incrementAndGet();
    final SpyEventHandler handler2 = e -> callCount2.incrementAndGet();
    final SpyEventHandler handler3 = e -> callCount3.incrementAndGet();
    spy.addSpyEventHandler( handler1 );
    spy.addSpyEventHandler( handler2 );
    spy.addSpyEventHandler( handler3 );

    assertEquals( spy.getSpyEventHandlers().size(), 3 );

    spy.reportSpyEvent( event );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    spy.reportSpyEvent( event );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount2.get(), 2 );
    assertEquals( callCount3.get(), 2 );
  }

  @Test
  public void onSpyEvent_whereOneHandlerGeneratesError()
  {
    final Spy spy = Arez.context().getSpy();

    final Object event = new Object();

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    final SpyEventHandler handler1 = e -> callCount1.incrementAndGet();
    final SpyEventHandler handler2 = e -> {
      throw exception;
    };
    final SpyEventHandler handler3 = e -> callCount3.incrementAndGet();
    spy.addSpyEventHandler( handler1 );
    spy.addSpyEventHandler( handler2 );
    spy.addSpyEventHandler( handler3 );

    spy.reportSpyEvent( event );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    final ArrayList<TestLogger.LogEntry> entries = getTestLogger().getEntries();
    assertEquals( entries.size(), 1 );
    final TestLogger.LogEntry entry1 = entries.get( 0 );
    assertEquals( entry1.getMessage(),
                  "Exception when notifying spy handler '" + handler2 + "' of '" + event + "' event." );
    assertEquals( entry1.getThrowable(), exception );

    spy.reportSpyEvent( event );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount3.get(), 2 );

    assertEquals( getTestLogger().getEntries().size(), 2 );
  }

  @Test
  public void isTransactionActive()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    assertEquals( spy.isTransactionActive(), false );

    setupReadOnlyTransaction( context );

    assertEquals( spy.isTransactionActive(), true );
  }

  @Test
  public void isActive()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    assertEquals( spy.isActive( computedValue ), false );
    setupReadOnlyTransaction( context );
    computedValue.getObserver().setState( Flags.STATE_UP_TO_DATE );
    assertEquals( spy.isActive( computedValue ), true );
  }

  @Test
  public void isComputing()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    assertEquals( spy.isComputing( computedValue ), false );
    computedValue.setComputing( true );
    assertEquals( spy.isComputing( computedValue ), true );
  }

  @Test
  public void getObservers()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputedValue<?> computedValue = context.computed( () -> "" );

    assertEquals( spy.getObservers( computedValue ).size(), 0 );

    final Observer observer = context.observer( new CountAndObserveProcedure() );
    observer.getDependencies().add( computedValue.getObservableValue() );
    computedValue.getObservableValue().getObservers().add( observer );

    assertEquals( spy.getObservers( computedValue ).size(), 1 );
    // Ensure the underlying list has the Observer in places
    assertEquals( computedValue.getObservableValue().getObservers().size(), 1 );

    assertUnmodifiable( spy.getObservers( computedValue ) );
  }

  @Test
  public void getTransactionComputing()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final SpyImpl spy = (SpyImpl) context.getSpy();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();
    final Observer observer2 = context.observer( new CountAndObserveProcedure() );

    computedValue.setComputing( true );

    final Transaction transaction =
      new Transaction( context, null, observer.getName(), observer.getMode(), observer );
    Transaction.setTransaction( transaction );

    // This picks up where it is the first transaction in stack
    assertEquals( spy.getTransactionComputing( computedValue ), transaction );

    final Transaction transaction2 =
      new Transaction( context, transaction, ValueUtil.randomString(), observer2.getMode(), observer2 );
    Transaction.setTransaction( transaction2 );

    // This picks up where it is not the first transaction in stack
    assertEquals( spy.getTransactionComputing( computedValue ), transaction );
  }

  @Test
  public void getTransactionComputing_missingTracker()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final SpyImpl spy = (SpyImpl) context.getSpy();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    computedValue.setComputing( true );

    setupReadOnlyTransaction( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getTransactionComputing( computedValue ) );

    assertEquals( exception.getMessage(),
                  "Arez-0106: ComputedValue named '" + computedValue.getName() + "' is marked as " +
                  "computing but unable to locate transaction responsible for computing ComputedValue" );
  }

  @Test
  public void getDependencies()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    assertEquals( spy.getDependencies( computedValue ).size(), 0 );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.getObservers().add( computedValue.getObserver() );
    computedValue.getObserver().getDependencies().add( observableValue );

    final List<ObservableValueInfo> dependencies = spy.getDependencies( computedValue );
    assertEquals( dependencies.size(), 1 );
    assertEquals( dependencies.iterator().next().getName(), observableValue.getName() );

    assertUnmodifiable( dependencies );
  }

  @Test
  public void getDependenciesDuringComputation()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    final ObservableValue<?> observableValue = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();
    final ObservableValue<?> observableValue3 = context.observable();

    observableValue.getObservers().add( computedValue.getObserver() );
    computedValue.getObserver().getDependencies().add( observableValue );

    computedValue.setComputing( true );

    setCurrentTransaction( computedValue.getObserver() );

    assertEquals( spy.getDependencies( computedValue ).size(), 0 );

    context.getTransaction().safeGetObservables().add( observableValue2 );
    context.getTransaction().safeGetObservables().add( observableValue3 );
    context.getTransaction().safeGetObservables().add( observableValue2 );

    final List<String> dependencies = spy.getDependencies( computedValue ).stream().
      map( ElementInfo::getName ).collect( Collectors.toList() );
    assertEquals( dependencies.size(), 2 );
    assertEquals( dependencies.contains( observableValue2.getName() ), true );
    assertEquals( dependencies.contains( observableValue3.getName() ), true );

    assertUnmodifiable( spy.getDependencies( computedValue ) );
  }

  @Test
  public void isComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    assertEquals( spy.isComputedValue( computedValue.getObservableValue() ),
                  true );
    assertEquals( spy.isComputedValue( context.observable() ), false );
  }

  @Test
  public void asComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final ObservableValue<?> observableValue = computedValue.getObservableValue();

    assertEquals( spy.asComputedValue( observableValue ).getName(), computedValue.getName() );
  }

  @Test
  public void getTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    setupReadOnlyTransaction( context );

    assertEquals( spy.getTransaction().getName(), context.getTransaction().getName() );
  }

  @Test
  public void getTransaction_whenNoTransaction()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final IllegalStateException exception = expectThrows( IllegalStateException.class, spy::getTransaction );

    assertEquals( exception.getMessage(),
                  "Arez-0105: Spy.getTransaction() invoked but no transaction active." );
  }

  @Test
  public void getComponent_Observable()
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );
    final ObservableValue<Object> observableValue1 =
      context.observable( component, ValueUtil.randomString(), null, null );
    final ObservableValue<Object> observableValue2 = context.observable();

    final ComponentInfo info = spy.getComponent( observableValue1 );
    assertNotNull( info );
    assertEquals( info.getName(), component.getName() );
    assertEquals( spy.getComponent( observableValue2 ), null );
  }

  @Test
  public void getComponent_Observable_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ObservableValue<Object> value = context.observable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getComponent( value ) );
    assertEquals( exception.getMessage(),
                  "Arez-0107: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void getComponent_ComputedValue()
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );
    final ComputedValue<Object> computedValue1 =
      context.computed( component, ValueUtil.randomString(), ValueUtil::randomString );
    final ComputedValue<Object> computedValue2 = context.computed( ValueUtil::randomString );

    final ComponentInfo info = spy.getComponent( computedValue1 );
    assertNotNull( info );
    assertEquals( info.getName(), component.getName() );
    assertEquals( spy.getComponent( computedValue2 ), null );
  }

  @Test
  public void getComponent_ComputedValue_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ComputedValue<Object> value = context.computed( () -> "" );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getComponent( value ) );
    assertEquals( exception.getMessage(),
                  "Arez-0109: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void getComponent_Observer()
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );
    final Observer observer1 =
      context.observer( component, null, AbstractArezTest::observeADependency );
    final Observer observer2 = context.observer( AbstractArezTest::observeADependency );

    final ComponentInfo info = spy.getComponent( observer1 );
    assertNotNull( info );
    assertEquals( info.getName(), component.getName() );
    assertEquals( spy.getComponent( observer2 ), null );
  }

  @Test
  public void getComponent_Observer_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Observer value = context.observer( AbstractArezTest::observeADependency );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getComponent( value ) );
    assertEquals( exception.getMessage(),
                  "Arez-0108: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
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
    assertEquals( spy.findAllComponentTypes().contains( type ), true );

    assertEquals( spy.findAllComponentsByType( ValueUtil.randomString() ).size(), 0 );

    final Collection<ComponentInfo> componentsByType1 = spy.findAllComponentsByType( type );
    assertEquals( componentsByType1.size(), 1 );
    assertEquals( componentsByType1.stream().anyMatch( c -> c.getName().equals( component.getName() ) ), true );

    final Component component2 = context.component( type, id2, ValueUtil.randomString() );

    assertEquals( spy.findAllComponentTypes().size(), 1 );
    assertEquals( spy.findAllComponentTypes().contains( type ), true );
    assertUnmodifiable( spy.findAllComponentTypes() );

    assertEquals( spy.findAllComponentsByType( ValueUtil.randomString() ).size(), 0 );

    final Collection<ComponentInfo> componentsByType2 = spy.findAllComponentsByType( type );
    assertUnmodifiable( componentsByType2 );
    assertEquals( componentsByType2.size(), 2 );
    assertEquals( componentsByType2.stream().anyMatch( c -> c.getName().equals( component.getName() ) ), true );
    assertEquals( componentsByType2.stream().anyMatch( c -> c.getName().equals( component2.getName() ) ), true );

    final ComponentInfo info1 = spy.findComponent( type, id1 );
    final ComponentInfo info2 = spy.findComponent( type, id2 );
    assertNotNull( info1 );
    assertNotNull( info2 );
    assertEquals( info1.getName(), component.getName() );
    assertEquals( info2.getName(), component2.getName() );
    assertEquals( spy.findComponent( type, ValueUtil.randomString() ), null );
    assertEquals( spy.findComponent( ValueUtil.randomString(), id2 ), null );
  }

  @Test
  public void findComponent_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.findComponent( type, id ) );

    assertEquals( exception.getMessage(),
                  "Arez-0010: ArezContext.findComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllComponentsByType_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final String type = ValueUtil.randomString();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.findAllComponentsByType( type ) );

    assertEquals( exception.getMessage(),
                  "Arez-0011: ArezContext.findAllComponentsByType() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllComponentTypes_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, spy::findAllComponentTypes );

    assertEquals( exception.getMessage(),
                  "Arez-0012: ArezContext.findAllComponentTypes() invoked when Arez.areNativeComponentsEnabled() returns false." );
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

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, spy::findAllTopLevelObservableValues );

    assertEquals( exception.getMessage(),
                  "Arez-0026: ArezContext.getTopLevelObservables() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void findAllTopLevelComputedValues()
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    final Spy spy = context.getSpy();

    final Collection<ComputedValueInfo> values = spy.findAllTopLevelComputedValues();
    assertEquals( values.size(), 1 );
    assertEquals( values.iterator().next().getName(), computedValue.getName() );
    assertUnmodifiable( values );

    assertEquals( spy.findAllTopLevelObservers().size(), 0 );
    assertEquals( spy.findAllTopLevelObservableValues().size(), 0 );
  }

  @Test
  public void findAllTopLevelComputedValues_registriesDisabled()
  {
    ArezTestUtil.disableRegistries();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, spy::findAllTopLevelComputedValues );

    assertEquals( exception.getMessage(),
                  "Arez-0036: ArezContext.getTopLevelComputedValues() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void findAllTopLevelObservers()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.observer( AbstractArezTest::observeADependency );

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

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, spy::findAllTopLevelObservers );

    assertEquals( exception.getMessage(),
                  "Arez-0031: ArezContext.getTopLevelObservers() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void observable_introspection()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final AtomicReference<String> value1 = new AtomicReference<>();
    value1.set( "23" );
    final AtomicReference<String> value2 = new AtomicReference<>();
    value2.set( "42" );

    final ObservableValue<String> observableValue1 =
      context.observable( ValueUtil.randomString(), value1::get, value1::set );
    final ObservableValue<String> observableValue2 = context.observable( ValueUtil.randomString(), value2::get, null );
    final ObservableValue<String> observableValue3 = context.observable( ValueUtil.randomString(), null, null );

    assertTrue( spy.hasAccessor( observableValue1 ) );
    assertTrue( spy.hasAccessor( observableValue2 ) );
    assertFalse( spy.hasAccessor( observableValue3 ) );

    assertTrue( spy.hasMutator( observableValue1 ) );
    assertFalse( spy.hasMutator( observableValue2 ) );
    assertFalse( spy.hasMutator( observableValue3 ) );

    assertEquals( spy.getValue( observableValue1 ), "23" );
    assertEquals( spy.getValue( observableValue2 ), "42" );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getValue( observableValue3 ) );
    assertEquals( exception.getMessage(),
                  "Arez-0112: Spy.getValue invoked on ObservableValue named '" + observableValue3.getName() + "' but " +
                  "ObservableValue has no property accessor." );

    spy.setValue( observableValue1, "71" );

    assertEquals( spy.getValue( observableValue1 ), "71" );

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> spy.setValue( observableValue2, "71" ) );
    assertEquals( exception2.getMessage(),
                  "Arez-0115: Spy.setValue invoked on ObservableValue named '" + observableValue2.getName() +
                  "' but ObservableValue has no property mutator." );
  }

  @Test
  public void observable_getValue_introspectorsDisabled()
    throws Throwable
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ObservableValue<Integer> computedValue1 = context.observable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.getValue( computedValue1 ) ) );
    assertEquals( exception2.getMessage(),
                  "Arez-0111: Spy.getValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void observable_hasAccessor_introspectorsDisabled()
    throws Throwable
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ObservableValue<Integer> observableValue = context.observable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.hasAccessor( observableValue ) ) );
    assertEquals( exception2.getMessage(),
                  "Arez-0110: Spy.hasAccessor invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void observable_hasMutator_introspectorsDisabled()
    throws Throwable
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ObservableValue<Integer> observableValue = context.observable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.hasMutator( observableValue ) ) );
    assertEquals( exception2.getMessage(),
                  "Arez-0113: Spy.hasMutator invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void observable_getValue_noAccessor()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ObservableValue<Integer> observableValue = context.observable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.getValue( observableValue ) ) );
    assertEquals( exception2.getMessage(),
                  "Arez-0112: Spy.getValue invoked on ObservableValue named '" + observableValue.getName() +
                  "' but ObservableValue has no property accessor." );
  }

  @Test
  public void observable_setValue_introspectorsDisabled()
    throws Throwable
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ObservableValue<Integer> computedValue1 = context.observable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.setValue( computedValue1, 44 ) ) );
    assertEquals( exception2.getMessage(),
                  "Arez-0114: Spy.setValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void observable_setValue_noMutator()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ObservableValue<Integer> observableValue = context.observable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.setValue( observableValue, 44 ) ) );
    assertEquals( exception2.getMessage(),
                  "Arez-0115: Spy.setValue invoked on ObservableValue named '" + observableValue.getName() +
                  "' but ObservableValue has no property mutator." );
  }

  @Test
  public void computedValue_introspection_noObservers()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue1 = context.computed( () -> {
      observeADependency();
      return "42";
    } );

    assertEquals( spy.getValue( computedValue1 ), null );
  }

  @Test
  public void computedValue_introspection()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final SafeFunction<String> function = () -> {
      observeADependency();
      return "42";
    };
    final ComputedValue<String> computedValue1 = context.computed( function );
    context.observer( computedValue1::get );

    assertEquals( spy.getValue( computedValue1 ), "42" );
  }

  @Test
  public void computedValue_getValue_introspectorsDisabled()
    throws Throwable
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ComputedValue<Integer> computedValue1 = context.computed( () -> 42 );

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.getValue( computedValue1 ) ) );
    assertEquals( exception2.getMessage(),
                  "Arez-0116: Spy.getValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
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
    final Observer observer = context.observer( AbstractArezTest::observeADependency );
    final ObserverInfo info = context.getSpy().asObserverInfo( observer );

    assertEquals( info.getName(), observer.getName() );
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
  public void asComputedValueInfo()
  {
    final ArezContext context = Arez.context();
    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final ComputedValueInfo info = context.getSpy().asComputedValueInfo( computedValue );

    assertEquals( info.getName(), computedValue.getName() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> list )
  {
    assertThrows( UnsupportedOperationException.class, () -> list.remove( list.iterator().next() ) );
  }

  private <T> void assertUnmodifiable( @Nonnull final List<T> list )
  {
    assertThrows( UnsupportedOperationException.class, () -> list.remove( 0 ) );
    assertThrows( UnsupportedOperationException.class, () -> list.add( list.get( 0 ) ) );
  }
}
