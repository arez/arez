package arez;

import arez.spy.ComponentInfo;
import arez.spy.ObservableValueInfo;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableValueInfoImplTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final ObservableValue<Object> observableValue = context.observable( name );
    final Observer observer = context.autorun( observableValue::reportObserved );

    final ObservableValueInfo info = observableValue.asInfo();

    assertEquals( info.getComponent(), null );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertEquals( info.hasAccessor(), false );
    assertEquals( info.hasMutator(), false );

    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().get( 0 ).getName(), observer.getName() );
    assertUnmodifiable( info.getObservers() );

    assertEquals( info.isComputedValue(), false );
    assertEquals( info.isDisposed(), false );

    // Dispose observer to avoid accessing observableValue when it is disposed
    observer.dispose();

    observableValue.dispose();

    assertEquals( info.isDisposed(), true );
  }

  @Test
  public void basicOperation_withIntrospectors()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final Component component = context.component( ValueUtil.randomString(), ValueUtil.randomString() );
    final AtomicReference<String> value = new AtomicReference<>();
    String initialValue = ValueUtil.randomString();
    value.set( initialValue );
    final ObservableValue<String> observableValue = context.observable( component, name, value::get, value::set );
    final Observer observer = context.autorun( observableValue::reportObserved );

    final ObservableValueInfo info = observableValue.asInfo();

    final ComponentInfo componentInfo = info.getComponent();
    assertNotNull( componentInfo );
    assertEquals( componentInfo.getType(), component.getType() );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertEquals( info.hasAccessor(), true );
    assertEquals( info.getValue(), initialValue );

    final String newValue = ValueUtil.randomString();

    assertEquals( info.hasMutator(), true );
    info.setValue( newValue );
    assertEquals( info.getValue(), newValue );

    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().get( 0 ).getName(), observer.getName() );
    assertUnmodifiable( info.getObservers() );

    assertEquals( info.isComputedValue(), false );
    assertEquals( info.isDisposed(), false );

    // Dispose observer to avoid accessing observableValue when it is disposed
    observer.dispose();

    observableValue.dispose();

    assertEquals( info.isDisposed(), true );
  }

  @Test
  public void asComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final ComputedValue<String> computedValue = context.computed( name, () -> "" );

    final ObservableValue<String> observableValue = computedValue.getObservableValue();

    final ObservableValueInfo info = observableValue.asInfo();

    assertEquals( info.getName(), name );

    assertEquals( info.isComputedValue(), true );
    assertEquals( info.asComputedValue().getName(), computedValue.getName() );
  }

  @SuppressWarnings( "EqualsWithItself" )
  @Test
  public void equalsAndHashCode()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observableValue1 = context.observable();
    final ObservableValue<Object> observableValue2 = context.observable();

    final ObservableValueInfo info1a = observableValue1.asInfo();
    final ObservableValueInfo info1b = new ObservableValueInfoImpl( context.getSpy(), observableValue1 );
    final ObservableValueInfo info2 = observableValue2.asInfo();

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

    assertEquals( info1a.hashCode(), observableValue1.hashCode() );
    assertEquals( info1a.hashCode(), info1b.hashCode() );
    assertEquals( info2.hashCode(), observableValue2.hashCode() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection )
  {
    assertThrows( UnsupportedOperationException.class, () -> collection.remove( collection.iterator().next() ) );
  }
}
