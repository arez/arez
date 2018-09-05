package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputedValueInfo;
import arez.spy.ElementInfo;
import arez.spy.ObservableValueInfo;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedValueInfoImplTest
  extends AbstractArezTest
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

    final ComputedValue<Object> computedValue =
      context.computed( name, () -> {
        observableValue.reportObserved();
        return value.get();
      } );
    final Observer observer = context.observer( computedValue::get );

    final ComputedValueInfo info = computedValue.asInfo();

    assertEquals( info.getComponent(), null );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertEquals( info.isActive(), true );
    assertEquals( info.isComputing(), false );
    assertEquals( info.getPriority(), Priority.NORMAL );

    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().get( 0 ).getName(), observer.getName() );
    assertUnmodifiable( info.getObservers() );

    assertEquals( info.getDependencies().size(), 1 );
    assertEquals( info.getDependencies().get( 0 ).getName(), observableValue.getName() );
    assertUnmodifiable( info.getDependencies() );

    assertEquals( info.getValue(), initialValue );

    assertEquals( info.isDisposed(), false );

    // Dispose observer so it does not access computedValue after it is disposed
    observer.dispose();

    computedValue.dispose();

    assertEquals( info.isDisposed(), true );
  }

  @Test
  public void isComputing()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    assertEquals( spy.asComputedValueInfo( computedValue ).isComputing(), false );
    computedValue.setComputing( true );
    assertEquals( spy.asComputedValueInfo( computedValue ).isComputing(), true );
  }

  @Test
  public void getTransactionComputing()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();
    final Observer observer2 = context.observer( new CountAndObserveProcedure() );

    computedValue.setComputing( true );
    final ComputedValueInfoImpl info = (ComputedValueInfoImpl) computedValue.asInfo();

    final Transaction transaction =
      new Transaction( context, null, observer.getName(), observer.getMode(), observer );
    Transaction.setTransaction( transaction );

    // This picks up where it is the first transaction in stack
    assertEquals( info.getTransactionComputing(), transaction );

    final Transaction transaction2 =
      new Transaction( context, transaction, ValueUtil.randomString(), observer2.getMode(), observer2 );
    Transaction.setTransaction( transaction2 );

    // This picks up where it is not the first transaction in stack
    assertEquals( info.getTransactionComputing(), transaction );
  }

  @Test
  public void getTransactionComputing_missingTracker()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    computedValue.setComputing( true );

    final ComputedValueInfoImpl info = (ComputedValueInfoImpl) computedValue.asInfo();
    setupReadOnlyTransaction( context );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, info::getTransactionComputing );

    assertEquals( exception.getMessage(),
                  "Arez-0106: ComputedValue named '" + computedValue.getName() + "' is marked as " +
                  "computing but unable to locate transaction responsible for computing ComputedValue" );
  }

  @Test
  public void getDependencies()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    final ComputedValueInfo info = computedValue.asInfo();

    assertEquals( info.getDependencies().size(), 0 );

    final ObservableValue<?> observableValue = context.observable();
    observableValue.getObservers().add( computedValue.getObserver() );
    computedValue.getObserver().getDependencies().add( observableValue );

    final List<ObservableValueInfo> dependencies = info.getDependencies();
    assertEquals( dependencies.size(), 1 );
    assertEquals( dependencies.iterator().next().getName(), observableValue.getName() );

    assertUnmodifiable( dependencies );
  }

  @Test
  public void getDependenciesDuringComputation()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    final ObservableValue<?> observableValue = context.observable();
    final ObservableValue<?> observableValue2 = context.observable();
    final ObservableValue<?> observableValue3 = context.observable();

    observableValue.getObservers().add( computedValue.getObserver() );
    computedValue.getObserver().getDependencies().add( observableValue );

    computedValue.setComputing( true );
    final ComputedValueInfo info = computedValue.asInfo();

    setCurrentTransaction( computedValue.getObserver() );

    assertEquals( info.getDependencies().size(), 0 );

    context.getTransaction().safeGetObservables().add( observableValue2 );
    context.getTransaction().safeGetObservables().add( observableValue3 );
    context.getTransaction().safeGetObservables().add( observableValue2 );

    final List<String> dependencies = info.getDependencies().stream().
      map( ElementInfo::getName ).collect( Collectors.toList() );
    assertEquals( dependencies.size(), 2 );
    assertEquals( dependencies.contains( observableValue2.getName() ), true );
    assertEquals( dependencies.contains( observableValue3.getName() ), true );

    assertUnmodifiable( info.getDependencies() );
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

    final ComponentInfo info = computedValue1.asInfo().getComponent();
    assertNotNull( info );
    assertEquals( info.getName(), component.getName() );
    assertEquals( computedValue2.asInfo().getComponent(), null );
  }

  @Test
  public void getComponent_ComputedValue_nativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();

    final ComputedValue<Object> computedValue = context.computed( () -> "" );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> computedValue.asInfo().getComponent() );
    assertEquals( exception.getMessage(),
                  "Arez-0109: Spy.getComponent invoked when Arez.areNativeComponentsEnabled() returns false." );
  }

  @Test
  public void isActive()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Spy spy = context.getSpy();

    final ComputedValue<String> computedValue = context.computed( () -> "" );

    assertEquals( spy.asComputedValueInfo( computedValue ).isActive(), false );
    setupReadOnlyTransaction( context );
    computedValue.getObserver().setState( Flags.STATE_UP_TO_DATE );
    assertEquals( spy.asComputedValueInfo( computedValue ).isActive(), true );
  }

  @SuppressWarnings( "EqualsWithItself" )
  @Test
  public void equalsAndHashCode()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ComputedValue<Object> computedValue1 = context.computed( () -> "1" );
    final ComputedValue<Object> computedValue2 = context.computed( () -> "2" );

    final ComputedValueInfo info1a = computedValue1.asInfo();
    final ComputedValueInfo info1b = new ComputedValueInfoImpl( context.getSpy(), computedValue1 );
    final ComputedValueInfo info2 = computedValue2.asInfo();

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

    assertEquals( info1a.hashCode(), computedValue1.hashCode() );
    assertEquals( info1a.hashCode(), info1b.hashCode() );
    assertEquals( info2.hashCode(), computedValue2.hashCode() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection )
  {
    assertThrows( UnsupportedOperationException.class, () -> collection.remove( collection.iterator().next() ) );
  }
}
