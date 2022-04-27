package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputableValueInfo;
import arez.spy.ElementInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.Priority;
import arez.spy.Spy;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComputableValueInfoImplTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final ObservableValue<Object> observableValue = context.observable();

    final AtomicReference<String> value = new AtomicReference<>();
    final String initialValue = ValueUtil.randomString();
    value.set( initialValue );

    final ComputableValue<Object> computableValue =
      context.computable( name, () -> {
        observableValue.reportObserved();
        return value.get();
      } );
    final Observer observer = context.observer( computableValue::get );

    final ComputableValueInfo info = computableValue.asInfo();

    assertNull( info.getComponent() );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertTrue( info.isActive() );
    assertFalse( info.isComputing() );
    assertEquals( info.getPriority(), Priority.NORMAL );

    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().get( 0 ).getName(), observer.getName() );
    assertUnmodifiable( info.getObservers() );

    assertEquals( info.getDependencies().size(), 1 );
    assertEquals( info.getDependencies().get( 0 ).getName(), observableValue.getName() );
    assertUnmodifiable( info.getDependencies() );

    assertEquals( info.getValue(), initialValue );

    assertFalse( info.isDisposed() );

    // Dispose observer so it does not access computableValue after it is disposed
    observer.dispose();

    computableValue.dispose();

    assertTrue( info.isDisposed() );
  }

  @Test
  public void isComputing()
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputableValue<String> computableValue = context.computable( () -> "" );

    assertFalse( spy.asComputableValueInfo( computableValue ).isComputing() );
    computableValue.setComputing( true );
    assertTrue( spy.asComputableValueInfo( computableValue ).isComputing() );
  }

  @Test
  public void getTransactionComputing()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();
    final Observer observer2 = context.observer( new CountAndObserveProcedure() );

    computableValue.setComputing( true );
    final ComputableValueInfoImpl info = (ComputableValueInfoImpl) computableValue.asInfo();

    final Transaction transaction =
      new Transaction( context, null, observer.getName(), observer.isMutation(), observer, false );
    Transaction.setTransaction( transaction );

    // This picks up where it is the first transaction in stack
    assertEquals( info.getTransactionComputing(), transaction );

    final Transaction transaction2 =
      new Transaction( context, transaction, ValueUtil.randomString(), observer2.isMutation(), observer2, false );
    Transaction.setTransaction( transaction2 );

    // This picks up where it is not the first transaction in stack
    assertEquals( info.getTransactionComputing(), transaction );
  }

  @Test
  public void getTransactionComputing_missingTracker()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );

    computableValue.setComputing( true );

    final ComputableValueInfoImpl info = (ComputableValueInfoImpl) computableValue.asInfo();
    setupReadOnlyTransaction( context );

    assertInvariantFailure( info::getTransactionComputing,
                            "Arez-0106: ComputableValue named '" + computableValue.getName() + "' is marked as " +
                            "computing but unable to locate transaction responsible for computing ComputableValue" );
  }

  @Test
  public void getDependencies()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );

    final ComputableValueInfo info = computableValue.asInfo();

    assertEquals( info.getDependencies().size(), 0 );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.getObservers().add( computableValue.getObserver() );
    computableValue.getObserver().getDependencies().add( observableValue );

    final List<ObservableValueInfo> dependencies = info.getDependencies();
    assertEquals( dependencies.size(), 1 );
    assertEquals( dependencies.iterator().next().getName(), observableValue.getName() );

    assertUnmodifiable( dependencies );
  }

  @Test
  public void getDependenciesDuringComputation()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );

    final ObservableValue<?> observableValue = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();
    final ObservableValue<?> observableValue3 = context.observable();

    observableValue.getObservers().add( computableValue.getObserver() );
    computableValue.getObserver().getDependencies().add( observableValue );

    computableValue.setComputing( true );
    final ComputableValueInfo info = computableValue.asInfo();

    setCurrentTransaction( computableValue.getObserver() );

    assertEquals( info.getDependencies().size(), 0 );

    context.getTransaction().safeGetObservables().add( observableValue2 );
    context.getTransaction().safeGetObservables().add( observableValue3 );
    context.getTransaction().safeGetObservables().add( observableValue2 );

    final List<String> dependencies = info.getDependencies().stream().map( ElementInfo::getName ).toList();
    assertEquals( dependencies.size(), 2 );
    assertTrue( dependencies.contains( observableValue2.getName() ) );
    assertTrue( dependencies.contains( observableValue3.getName() ) );

    assertUnmodifiable( info.getDependencies() );
  }

  @Test
  public void getComponent_ComputableValue()
  {
    final ArezContext context = Arez.context();

    final Component component =
      context.component( ValueUtil.randomString(), ValueUtil.randomString(), ValueUtil.randomString() );
    final ComputableValue<Object> computableValue1 =
      context.computable( component, ValueUtil.randomString(), ValueUtil::randomString );
    final ComputableValue<Object> computableValue2 = context.computable( ValueUtil::randomString );

    final ComponentInfo info = computableValue1.asInfo().getComponent();
    assertNotNull( info );
    assertEquals( info.getName(), component.getName() );
    assertNull( computableValue2.asInfo().getComponent() );
  }

  @Test
  public void getComponent_ComputableValue_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final ComputableValue<Object> computableValue = context.computable( () -> "" );

    assertInvariantFailure( () -> computableValue.asInfo().getComponent(),
                            "Arez-0109: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void isActive()
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputableValue<String> computableValue = context.computable( () -> "" );

    assertFalse( spy.asComputableValueInfo( computableValue ).isActive() );
    setupReadOnlyTransaction( context );
    computableValue.getObserver().setState( Observer.Flags.STATE_UP_TO_DATE );
    assertTrue( spy.asComputableValueInfo( computableValue ).isActive() );
  }

  @Test
  public void getObservers()
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputableValue<?> computableValue = context.computable( () -> "" );

    assertEquals( spy.asComputableValueInfo( computableValue ).getObservers().size(), 0 );

    final Observer observer = context.observer( new CountAndObserveProcedure() );
    observer.getDependencies().add( computableValue.getObservableValue() );
    computableValue.getObservableValue().getObservers().add( observer );

    assertEquals( spy.asComputableValueInfo( computableValue ).getObservers().size(), 1 );
    // Ensure the underlying list has the Observer in places
    assertEquals( computableValue.getObservableValue().getObservers().size(), 1 );

    assertUnmodifiable( spy.asComputableValueInfo( computableValue ).getObservers() );
  }

  @Test
  public void computableValue_introspection_noObservers()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue1 = context.computable( () -> {
      observeADependency();
      return "42";
    } );

    assertNull( computableValue1.asInfo().getValue() );
  }

  @Test
  public void computableValue_introspection()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SafeFunction<String> function = () -> {
      observeADependency();
      return "42";
    };
    final ComputableValue<String> computableValue1 = context.computable( function );
    context.observer( computableValue1::get );

    assertEquals( computableValue1.asInfo().getValue(), "42" );
  }

  @Test
  public void computableValue_getValue_introspectorsDisabled()
  {
    ArezTestUtil.disablePropertyIntrospectors();

    final ArezContext context = Arez.context();

    final ComputableValue<Integer> computableValue1 = context.computable( () -> 42 );

    assertInvariantFailure( () -> context.action( () -> computableValue1.asInfo().getValue() ),
                            "Arez-0116: Spy.getValue invoked when Arez.arePropertyIntrospectorsEnabled() returns false." );
  }

  @SuppressWarnings( "EqualsWithItself" )
  @Test
  public void equalsAndHashCode()
  {
    final ArezContext context = Arez.context();
    final ComputableValue<Object> computableValue1 = context.computable( () -> "1" );
    final ComputableValue<Object> computableValue2 = context.computable( () -> "2" );

    final ComputableValueInfo info1a = computableValue1.asInfo();
    final ComputableValueInfo info1b = new ComputableValueInfoImpl( computableValue1 );
    final ComputableValueInfo info2 = computableValue2.asInfo();

    //noinspection EqualsBetweenInconvertibleTypes
    assertNotEquals( info1a, "" );

    assertEquals( info1a, info1a );
    assertEquals( info1b, info1a );
    assertNotEquals( info2, info1a );

    assertEquals( info1a, info1b );
    assertEquals( info1b, info1b );
    assertNotEquals( info2, info1b );

    assertNotEquals( info1a, info2 );
    assertNotEquals( info1b, info2 );
    assertEquals( info2, info2 );

    assertEquals( info1a.hashCode(), computableValue1.hashCode() );
    assertEquals( info1a.hashCode(), info1b.hashCode() );
    assertEquals( info2.hashCode(), computableValue2.hashCode() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection )
  {
    assertThrows( UnsupportedOperationException.class, () -> collection.remove( collection.iterator().next() ) );
  }
}
