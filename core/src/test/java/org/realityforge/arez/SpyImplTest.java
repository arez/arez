package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.arez.spy.ComponentInfo;
import org.realityforge.arez.spy.ComputedValueInfo;
import org.realityforge.arez.spy.ElementInfo;
import org.realityforge.arez.spy.ObservableInfo;
import org.realityforge.arez.spy.ObserverInfo;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SpyImplTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
    throws Exception
  {
    final SpyImpl spy = new SpyImpl( new ArezContext() );

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
    final SpyImpl spy = new SpyImpl( new ArezContext() );

    assertFalse( spy.willPropagateSpyEvents() );

    final Object event = new Object();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.reportSpyEvent( event ) );

    assertEquals( exception.getMessage(),
                  "Attempting to report SpyEvent '" + event + "' but willPropagateSpyEvents() returns false." );
  }

  @Test
  public void addSpyEventHandler_alreadyExists()
    throws Exception
  {
    final SpyImpl support = new SpyImpl( new ArezContext() );

    final SpyEventHandler handler = new TestSpyEventHandler();
    support.addSpyEventHandler( handler );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> support.addSpyEventHandler( handler ) );

    assertEquals( exception.getMessage(),
                  "Attempting to add handler " + handler + " that is already in the list of spy handlers." );
  }

  @Test
  public void removeSpyEventHandler_noExists()
    throws Exception
  {
    final SpyImpl support = new SpyImpl( new ArezContext() );

    final SpyEventHandler handler = new TestSpyEventHandler();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> support.removeSpyEventHandler( handler ) );

    assertEquals( exception.getMessage(),
                  "Attempting to remove handler " + handler + " that is not in the list of spy handlers." );
  }

  @Test
  public void multipleHandlers()
  {
    final SpyImpl support = new SpyImpl( new ArezContext() );

    final Object event = new Object();

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount2 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final SpyEventHandler handler1 = e -> callCount1.incrementAndGet();
    final SpyEventHandler handler2 = e -> callCount2.incrementAndGet();
    final SpyEventHandler handler3 = e -> callCount3.incrementAndGet();
    support.addSpyEventHandler( handler1 );
    support.addSpyEventHandler( handler2 );
    support.addSpyEventHandler( handler3 );

    assertEquals( support.getSpyEventHandlers().size(), 3 );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount2.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount2.get(), 2 );
    assertEquals( callCount3.get(), 2 );
  }

  @Test
  public void onSpyEvent_whereOneHandlerGeneratesError()
  {
    final SpyImpl support = new SpyImpl( new ArezContext() );

    final Object event = new Object();

    final AtomicInteger callCount1 = new AtomicInteger();
    final AtomicInteger callCount3 = new AtomicInteger();

    final RuntimeException exception = new RuntimeException( "X" );

    final SpyEventHandler handler1 = e -> callCount1.incrementAndGet();
    final SpyEventHandler handler2 = e -> {
      throw exception;
    };
    final SpyEventHandler handler3 = e -> callCount3.incrementAndGet();
    support.addSpyEventHandler( handler1 );
    support.addSpyEventHandler( handler2 );
    support.addSpyEventHandler( handler3 );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 1 );
    assertEquals( callCount3.get(), 1 );

    final ArrayList<TestLogger.LogEntry> entries = getTestLogger().getEntries();
    assertEquals( entries.size(), 1 );
    final TestLogger.LogEntry entry1 = entries.get( 0 );
    assertEquals( entry1.getMessage(),
                  "Exception when notifying spy handler '" + handler2 + "' of '" + event + "' event." );
    assertEquals( entry1.getThrowable(), exception );

    support.reportSpyEvent( event );

    assertEquals( callCount1.get(), 2 );
    assertEquals( callCount3.get(), 2 );

    assertEquals( getTestLogger().getEntries().size(), 2 );
  }

  @Test
  public void isTransactionActive()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    assertEquals( spy.isTransactionActive(), false );

    setCurrentTransaction( context );

    assertEquals( spy.isTransactionActive(), true );
  }

  @Test
  public void isActive()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    assertEquals( spy.isActive( computedValue ), false );
    setCurrentTransaction( context );
    computedValue.getObserver().setState( ObserverState.UP_TO_DATE );
    assertEquals( spy.isActive( computedValue ), true );
  }

  @Test
  public void isComputing()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    assertEquals( spy.isComputing( computedValue ), false );
    computedValue.setComputing( true );
    assertEquals( spy.isComputing( computedValue ), true );
  }

  @Test
  public void getObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final ComputedValue<?> computedValue = newDerivation( context ).getComputedValue();

    assertEquals( spy.getObservers( computedValue ).size(), 0 );

    final Observer observer = newReadOnlyObserver( context );
    observer.getDependencies().add( computedValue.getObservable() );
    computedValue.getObservable().getObservers().add( observer );

    assertEquals( spy.getObservers( computedValue ).size(), 1 );
    // Ensure the underlying list has the Observer in places
    assertEquals( computedValue.getObservable().getObservers().size(), 1 );

    assertUnmodifiable( spy.getObservers( computedValue ) );
  }

  @Test
  public void getTransactionComputing()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final Observer observer2 = newReadOnlyObserver( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

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
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    computedValue.setComputing( true );

    setCurrentTransaction( context );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getTransactionComputing( computedValue ) );

    assertEquals( exception.getMessage(),
                  "ComputedValue named '" + computedValue.getName() + "' is marked as computing but unable " +
                  "to locate transaction responsible for computing ComputedValue" );
  }

  @Test
  public void getDependencies()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    assertEquals( spy.getDependencies( computedValue ).size(), 0 );

    final Observable<?> observable = newObservable( context );
    observable.getObservers().add( computedValue.getObserver() );
    computedValue.getObserver().getDependencies().add( observable );

    final List<ObservableInfo> dependencies = spy.getDependencies( computedValue );
    assertEquals( dependencies.size(), 1 );
    assertEquals( dependencies.iterator().next().getName(), observable.getName() );

    assertUnmodifiable( dependencies );
  }

  @Test
  public void getDependenciesDuringComputation()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    final Observable<?> observable = newObservable( context );
    final Observable<?> observable2 = newObservable( context );
    final Observable<?> observable3 = newObservable( context );

    observable.getObservers().add( computedValue.getObserver() );
    computedValue.getObserver().getDependencies().add( observable );

    computedValue.setComputing( true );

    setCurrentTransaction( computedValue.getObserver() );

    assertEquals( spy.getDependencies( computedValue ).size(), 0 );

    context.getTransaction().safeGetObservables().add( observable2 );
    context.getTransaction().safeGetObservables().add( observable3 );
    context.getTransaction().safeGetObservables().add( observable2 );

    final List<String> dependencies = spy.getDependencies( computedValue ).stream().
      map( ElementInfo::getName ).collect( Collectors.toList() );
    assertEquals( dependencies.size(), 2 );
    assertEquals( dependencies.contains( observable2.getName() ), true );
    assertEquals( dependencies.contains( observable3.getName() ), true );

    assertUnmodifiable( spy.getDependencies( computedValue ) );
  }

  @Test
  public void isComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    assertEquals( spy.isComputedValue( newDerivation( context ).getDerivedValue() ), true );
    assertEquals( spy.isComputedValue( newObservable( context ) ), false );
  }

  @Test
  public void asComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final Observable<?> observable = observer.getDerivedValue();
    final ComputedValue<?> computedValue = observer.getComputedValue();

    assertEquals( spy.asComputedValue( observable ).getName(), computedValue.getName() );
  }

  @Test
  public void Observable_getObservers()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observable<?> observable = newObservable( context );

    assertEquals( spy.getObservers( observable ).size(), 0 );

    final Observer observer = newReadOnlyObserver( context );
    observable.getObservers().add( observer );

    final List<ObserverInfo> observers = spy.getObservers( observable );
    assertEquals( observers.size(), 1 );
    assertEquals( observers.iterator().next().getName(), observer.getName() );

    assertUnmodifiable( observers );
  }

  @Test
  public void Observer_isComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    assertEquals( spy.isComputedValue( newDerivation( context ) ), true );
    assertEquals( spy.isComputedValue( newReadOnlyObserver( context ) ), false );
  }

  @Test
  public void Observer_asComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    assertEquals( spy.asComputedValue( observer ).getName(), computedValue.getName() );
  }

  @Test
  public void getTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    setCurrentTransaction( context );

    assertEquals( spy.getTransaction().getName(), context.getTransaction().getName() );
  }

  @Test
  public void getTransaction_whenNoTransaction()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, spy::getTransaction );

    assertEquals( exception.getMessage(),
                  "Spy.getTransaction() invoked but no transaction active." );
  }

  @Test
  public void isRunning()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newReadOnlyObserver( context );

    assertEquals( spy.isRunning( observer ), false );

    setCurrentTransaction( observer );

    assertEquals( spy.isRunning( observer ), true );

    setCurrentTransaction( context );

    assertEquals( spy.isRunning( observer ), false );
  }

  @Test
  public void isScheduled()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newReadOnlyObserver( context );

    assertEquals( spy.isScheduled( observer ), false );

    observer.markAsScheduled();

    assertEquals( spy.isScheduled( observer ), true );
  }

  @Test
  public void Observer_getDependencies()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newReadOnlyObserver( context );

    assertEquals( spy.getDependencies( observer ).size(), 0 );

    final Observable<?> observable = newObservable( context );
    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    final List<ObservableInfo> dependencies = spy.getDependencies( observer );
    assertEquals( dependencies.size(), 1 );
    assertEquals( dependencies.get( 0 ).getName(), observable.getName() );

    assertUnmodifiable( dependencies );
  }

  @Test
  public void Ovserver_getDependenciesWhileRunning()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    final Observer observer = newReadOnlyObserver( context );

    final Observable<?> observable = newObservable( context );
    final Observable<?> observable2 = newObservable( context );
    final Observable<?> observable3 = newObservable( context );

    observable.getObservers().add( observer );
    observer.getDependencies().add( observable );

    setCurrentTransaction( observer );

    assertEquals( spy.getDependencies( observer ).size(), 0 );

    context.getTransaction().safeGetObservables().add( observable2 );
    context.getTransaction().safeGetObservables().add( observable3 );
    context.getTransaction().safeGetObservables().add( observable2 );

    final List<String> dependencies = spy.getDependencies( observer ).stream().
      map( ElementInfo::getName ).collect( Collectors.toList() );
    assertEquals( dependencies.size(), 2 );
    assertEquals( dependencies.contains( observable2.getName() ), true );
    assertEquals( dependencies.contains( observable3.getName() ), true );

    assertUnmodifiable( spy.getDependencies( observer ) );
  }

  @Test
  public void isReadOnly()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final SpyImpl spy = new SpyImpl( context );

    assertEquals( spy.isReadOnly( newReadOnlyObserver( context ) ), true );
    assertEquals( spy.isReadOnly( newDerivation( context ) ), true );
    assertEquals( spy.isReadOnly( newReadWriteObserver( context ) ), false );
  }

  @Test
  public void getComponent_Observable()
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Component component =
      context.createComponent( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );
    final Observable<Object> observable1 = context.createObservable( component, ValueUtil.randomString(), null, null );
    final Observable<Object> observable2 = context.createObservable();

    final ComponentInfo info = spy.getComponent( observable1 );
    assertNotNull( info );
    assertEquals( info.getName(), component.getName() );
    assertEquals( spy.getComponent( observable2 ), null );
  }

  @Test
  public void getComponent_Observable_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Observable<Object> value = context.createObservable();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getComponent( value ) );
    assertEquals( exception.getMessage(),
                  "Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void getComponent_ComputedValue()
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Component component =
      context.createComponent( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );
    final ComputedValue<Object> computedValue1 =
      context.createComputedValue( component,
                                   ValueUtil.randomString(),
                                   () -> "",
                                   Objects::equals,
                                   null,
                                   null,
                                   null,
                                   null );
    final ComputedValue<Object> computedValue2 = context.createComputedValue( () -> "" );

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

    final ComputedValue<Object> value = context.createComputedValue( () -> "" );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getComponent( value ) );
    assertEquals( exception.getMessage(),
                  "Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void getComponent_Observer()
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Component component =
      context.createComponent( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );
    final Observer observer1 =
      context.autorun( component,
                       ValueUtil.randomString(),
                       true,
                       () -> {
                       },
                       true );
    final Observer observer2 = context.autorun( () -> {
    } );

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

    final Observer value = context.autorun( () -> {
    } );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getComponent( value ) );
    assertEquals( exception.getMessage(),
                  "Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
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

    final Component component = context.createComponent( type, id1, ValueUtil.randomString() );

    assertEquals( spy.findAllComponentTypes().size(), 1 );
    assertEquals( spy.findAllComponentTypes().contains( type ), true );

    assertEquals( spy.findAllComponentsByType( ValueUtil.randomString() ).size(), 0 );

    final Collection<ComponentInfo> componentsByType1 = spy.findAllComponentsByType( type );
    assertEquals( componentsByType1.size(), 1 );
    assertEquals( componentsByType1.stream().anyMatch( c -> c.getName().equals( component.getName() ) ), true );

    final Component component2 = context.createComponent( type, id2, ValueUtil.randomString() );

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
                  "ArezContext.findComponent() invoked when Arez.areNativeComponentsEnabled() returns false." );
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
                  "ArezContext.findAllComponentsByType() invoked when Arez.areNativeComponentsEnabled() returns false." );
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
                  "ArezContext.findAllComponentTypes() invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void findAllTopLevelObservables()
  {
    final ArezContext context = Arez.context();

    final Observable<String> observable = context.createObservable();

    final Spy spy = context.getSpy();

    final Collection<ObservableInfo> values = spy.findAllTopLevelObservables();
    assertEquals( values.size(), 1 );
    assertEquals( values.iterator().next().getName(), observable.getName() );
    assertUnmodifiable( values );
  }

  @Test
  public void findAllTopLevelObservables_registriesDisabled()
  {
    ArezTestUtil.disableRegistries();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, spy::findAllTopLevelObservables );

    assertEquals( exception.getMessage(),
                  "ArezContext.getTopLevelObservables() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void findAllTopLevelComputedValues()
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.createComputedValue( () -> "" );

    final Spy spy = context.getSpy();

    final Collection<ComputedValueInfo> values = spy.findAllTopLevelComputedValues();
    assertEquals( values.size(), 1 );
    assertEquals( values.iterator().next().getName(), computedValue.getName() );
    assertUnmodifiable( values );

    assertEquals( spy.findAllTopLevelObservers().size(), 0 );
    assertEquals( spy.findAllTopLevelObservables().size(), 0 );
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
                  "ArezContext.getTopLevelComputedValues() invoked when Arez.areRegistriesEnabled() returns false." );
  }

  @Test
  public void findAllTopLevelObservers()
  {
    final ArezContext context = Arez.context();

    final Observer observer = context.autorun( () -> {
    } );

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
                  "ArezContext.getTopLevelObservers() invoked when Arez.areRegistriesEnabled() returns false." );
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

    final Observable<String> observable1 =
      context.createObservable( ValueUtil.randomString(), value1::get, value1::set );
    final Observable<String> observable2 = context.createObservable( ValueUtil.randomString(), value2::get, null );
    final Observable<String> observable3 = context.createObservable( ValueUtil.randomString(), null, null );

    assertTrue( spy.hasAccessor( observable1 ) );
    assertTrue( spy.hasAccessor( observable2 ) );
    assertFalse( spy.hasAccessor( observable3 ) );

    assertTrue( spy.hasMutator( observable1 ) );
    assertFalse( spy.hasMutator( observable2 ) );
    assertFalse( spy.hasMutator( observable3 ) );

    assertEquals( spy.getValue( observable1 ), "23" );
    assertEquals( spy.getValue( observable2 ), "42" );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.getValue( observable3 ) );
    assertEquals( exception.getMessage(),
                  "Spy.getValue invoked on observable named '" + observable3.getName() + "' but " +
                  "observable has no property accessor." );

    spy.setValue( observable1, "71" );

    assertEquals( spy.getValue( observable1 ), "71" );

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> spy.setValue( observable2, "71" ) );
    assertEquals( exception2.getMessage(),
                  "Spy.setValue invoked on observable named '" + observable2.getName() + "' but " +
                  "observable has no property mutator." );
  }

  @Test
  public void observable_getValue_introspectorsDisabled()
    throws Throwable
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Observable<Integer> computedValue1 = context.createObservable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.getValue( computedValue1 ) ) );
    assertEquals( exception2.getMessage(),
                  "Spy.getValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void observable_hasAccessor_introspectorsDisabled()
    throws Throwable
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Observable<Integer> observable = context.createObservable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.hasAccessor( observable ) ) );
    assertEquals( exception2.getMessage(),
                  "Spy.hasAccessor invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void observable_hasMutator_introspectorsDisabled()
    throws Throwable
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Observable<Integer> observable = context.createObservable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.hasMutator( observable ) ) );
    assertEquals( exception2.getMessage(),
                  "Spy.hasMutator invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void observable_getValue_noAccessor()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Observable<Integer> observable = context.createObservable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.getValue( observable ) ) );
    assertEquals( exception2.getMessage(),
                  "Spy.getValue invoked on observable named '" + observable.getName() +
                  "' but observable has no property accessor." );
  }

  @Test
  public void observable_setValue_introspectorsDisabled()
    throws Throwable
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Observable<Integer> computedValue1 = context.createObservable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.setValue( computedValue1, 44 ) ) );
    assertEquals( exception2.getMessage(),
                  "Spy.setValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void observable_setValue_noMutator()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Observable<Integer> observable = context.createObservable();

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.setValue( observable, 44 ) ) );
    assertEquals( exception2.getMessage(),
                  "Spy.setValue invoked on observable named '" + observable.getName() +
                  "' but observable has no property mutator." );
  }

  @Test
  public void computedValue_introspection_noObservers()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue1 = context.createComputedValue( () -> "42" );

    assertEquals( context.action( () -> spy.getValue( computedValue1 ) ), null );
  }

  @Test
  public void computedValue_introspection()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue1 = context.createComputedValue( () -> "42" );
    context.autorun( computedValue1::get );

    assertEquals( context.action( () -> spy.getValue( computedValue1 ) ), "42" );
  }

  @Test
  public void computedValue_getValue_introspectorsDisabled()
    throws Throwable
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final ComputedValue<Integer> computedValue1 = context.createComputedValue( () -> 42 );

    final IllegalStateException exception2 =
      expectThrows( IllegalStateException.class, () -> context.action( () -> spy.getValue( computedValue1 ) ) );
    assertEquals( exception2.getMessage(),
                  "Spy.getValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @Test
  public void asComponentInfo()
  {
    final ArezContext context = Arez.context();
    final Component component = context.createComponent( ValueUtil.randomString(), ValueUtil.randomString() );
    final ComponentInfo info = context.getSpy().asComponentInfo( component );

    assertEquals( info.getName(), component.getName() );
  }

  @Test
  public void asObserverInfo()
  {
    final ArezContext context = Arez.context();
    final Observer observer = context.autorun( () -> {
    } );
    final ObserverInfo info = context.getSpy().asObserverInfo( observer );

    assertEquals( info.getName(), observer.getName() );
  }

  @Test
  public void asObservableInfo()
  {
    final ArezContext context = Arez.context();
    final Observable<Object> observable = context.createObservable( ValueUtil.randomString() );
    final ObservableInfo info = context.getSpy().asObservableInfo( observable );

    assertEquals( info.getName(), observable.getName() );
  }

  @Test
  public void asComputedValueInfo()
  {
    final ArezContext context = Arez.context();
    final ComputedValue<String> computedValue = context.createComputedValue( () -> "" );
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
