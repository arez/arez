package arez;

import arez.spy.ComponentInfo;
import arez.spy.ElementInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserverInfoImplTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final ObservableValue<Object> observableValue = context.observable();
    final Observer observer = context.observer( name, observableValue::reportObserved );

    final ObserverInfo info = observer.asInfo();

    assertEquals( info.getComponent(), null );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertEquals( info.getDependencies().size(), 1 );
    assertEquals( info.getDependencies().get( 0 ).getName(), observableValue.getName() );
    assertUnmodifiable( info.getDependencies() );

    assertEquals( info.isActive(), true );
    assertEquals( info.isComputedValue(), false );
    assertEquals( info.isReadOnly(), true );
    assertEquals( info.getPriority(), Priority.NORMAL );
    assertEquals( info.isRunning(), false );
    assertEquals( info.isScheduled(), false );
    assertEquals( info.isDisposed(), false );

    observer.dispose();

    assertEquals( info.isDisposed(), true );
    assertEquals( info.isActive(), false );
  }

  @Test
  public void isScheduled()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final ObserverInfo info = spy.asObserverInfo( observer );

    assertEquals( info.isScheduled(), false );

    observer.setScheduledFlag();

    assertEquals( info.isScheduled(), true );
  }

  @Test
  public void isRunning()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();
    final AtomicReference<ObserverInfo> ref = new AtomicReference<>();

    final Observer observer = context.observer( () -> {
      assertEquals( ref.get().isRunning(), true );
      callCount.incrementAndGet();
      observeADependency();
    }, Options.DEFER_REACT );
    final ObserverInfo info = context.getSpy().asObserverInfo( observer );
    ref.set( info );

    assertEquals( info.isRunning(), false );
    assertEquals( callCount.get(), 0 );

    context.triggerScheduler();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void isReadOnly_on_READ_WRITE_observer()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observableValue = context.observable();
    final Observer observer = context.observer( observableValue::reportObserved, Options.READ_WRITE );

    assertEquals( observer.asInfo().isReadOnly(), false );
  }

  @Test
  public void getDependencies()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( observable::reportObserved );

    final List<ObservableValueInfo> dependencies = spy.asObserverInfo( observer ).getDependencies();
    assertEquals( dependencies.size(), 1 );
    assertEquals( dependencies.get( 0 ).getName(), observable.getName() );

    assertUnmodifiable( dependencies );
  }

  @Test
  public void Ovserver_getDependenciesWhileRunning()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    final ObservableValue<?> observableValue = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();
    final ObservableValue<?> observableValue3 = context.observable();

    observableValue.getObservers().add( observer );
    observer.getDependencies().add( observableValue );

    setCurrentTransaction( observer );

    assertEquals( spy.asObserverInfo( observer ).getDependencies().size(), 0 );

    context.getTransaction().safeGetObservables().add( observableValue2 );
    context.getTransaction().safeGetObservables().add( observableValue3 );
    context.getTransaction().safeGetObservables().add( observableValue2 );

    final List<String> dependencies = spy.asObserverInfo( observer ).getDependencies().stream().
      map( ElementInfo::getName ).collect( Collectors.toList() );
    assertEquals( dependencies.size(), 2 );
    assertEquals( dependencies.contains( observableValue2.getName() ), true );
    assertEquals( dependencies.contains( observableValue3.getName() ), true );

    assertUnmodifiable( spy.asObserverInfo( observer ).getDependencies() );
  }

  @Test
  public void asComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final ComputedValue<String> computedValue = context.computed( name, () -> "" );

    final Observer observer = computedValue.getObserver();

    final ObserverInfo info = observer.asInfo();

    assertEquals( info.getName(), name );

    assertEquals( info.isComputedValue(), true );
    assertEquals( info.asComputedValue().getName(), computedValue.getName() );

    // Not yet observed
    assertEquals( info.isActive(), false );
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

    final ComponentInfo info = spy.asObserverInfo( observer1 ).getComponent();
    assertNotNull( info );
    assertEquals( info.getName(), component.getName() );
    assertEquals( spy.asObserverInfo( observer2 ).getComponent(), null );
  }

  @Test
  public void getComponent_Observer_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Spy spy = context.getSpy();

    final Observer observer = context.observer( AbstractArezTest::observeADependency );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> spy.asObserverInfo( observer ).getComponent() );
    assertEquals( exception.getMessage(),
                  "Arez-0108: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @SuppressWarnings( "EqualsWithItself" )
  @Test
  public void equalsAndHashCode()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observableValue = context.observable();
    final Observer observer1 = context.observer( ValueUtil.randomString(), observableValue::reportObserved );
    final Observer observer2 = context.observer( ValueUtil.randomString(), observableValue::reportObserved );

    final ObserverInfo info1a = observer1.asInfo();
    final ObserverInfo info1b = new ObserverInfoImpl( context.getSpy(), observer1 );
    final ObserverInfo info2 = observer2.asInfo();

    //noinspection EqualsBetweenInconvertibleTypes
    assertEquals( info1a.equals( "" ), false );

    assertEquals( info1a.equals( info1a ), true );
    assertEquals( info1a.equals( info1b ), true );
    assertEquals( info1a.equals( info2 ), false );

    assertEquals( info1b.equals( info1a ), true );
    assertEquals( info1b.equals( info1b ), true );
    assertEquals( info1b.equals( info2 ), false );

    assertEquals( info2.equals( info1a ), false );
    assertEquals( info2.equals( info1b ), false );
    assertEquals( info2.equals( info2 ), true );

    assertEquals( info1a.hashCode(), observer1.hashCode() );
    assertEquals( info1a.hashCode(), info1b.hashCode() );
    assertEquals( info2.hashCode(), observer2.hashCode() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection )
  {
    assertThrows( UnsupportedOperationException.class, () -> collection.remove( collection.iterator().next() ) );
  }
}
