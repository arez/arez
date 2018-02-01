package arez;

import arez.spy.ComponentInfo;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservableInfoImplTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final Observable<Object> observable = context.createObservable( name );
    final Observer observer = context.autorun( observable::reportObserved );

    final ObservableInfoImpl info = new ObservableInfoImpl( context.getSpy(), observable );

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

    // Dispose observer to avoid accessing observable when it is disposed
    observer.dispose();

    observable.dispose();

    assertEquals( info.isDisposed(), true );
  }

  @Test
  public void basicOperation_withIntrospectors()
    throws Throwable
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final Component component = context.createComponent( ValueUtil.randomString(), ValueUtil.randomString() );
    final AtomicReference<String> value = new AtomicReference<>();
    String initialValue = ValueUtil.randomString();
    value.set( initialValue );
    final Observable<String> observable = context.createObservable( component, name, value::get, value::set );
    final Observer observer = context.autorun( observable::reportObserved );

    final ObservableInfoImpl info = new ObservableInfoImpl( context.getSpy(), observable );

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

    // Dispose observer to avoid accessing observable when it is disposed
    observer.dispose();

    observable.dispose();

    assertEquals( info.isDisposed(), true );
  }

  @Test
  public void asComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final ComputedValue<String> computedValue = context.createComputedValue( name, () -> "" );

    final Observable<String> observable = computedValue.getObservable();

    final ObservableInfoImpl info = new ObservableInfoImpl( context.getSpy(), observable );

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
    final Observable<Object> observable1 = context.createObservable();
    final Observable<Object> observable2 = context.createObservable();

    final ObservableInfoImpl info1a = new ObservableInfoImpl( context.getSpy(), observable1 );
    final ObservableInfoImpl info1b = new ObservableInfoImpl( context.getSpy(), observable1 );
    final ObservableInfoImpl info2 = new ObservableInfoImpl( context.getSpy(), observable2 );

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

    assertEquals( info1a.hashCode(), observable1.hashCode() );
    assertEquals( info1a.hashCode(), info1b.hashCode() );
    assertEquals( info2.hashCode(), observable2.hashCode() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection )
  {
    assertThrows( UnsupportedOperationException.class, () -> collection.remove( collection.iterator().next() ) );
  }
}
