package org.realityforge.arez;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
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
    final Observable<Object> observable = context.createObservable();

    final AtomicReference<String> value = new AtomicReference<>();
    final String initialValue = ValueUtil.randomString();
    value.set( initialValue );

    final ComputedValue<Object> computedValue =
      context.createComputedValue( name, () -> {
        observable.reportObserved();
        return value.get();
      } );
    final Observer observer = context.autorun( computedValue::get );

    final ComputedValueInfoImpl info = new ComputedValueInfoImpl( context.getSpy(), computedValue );

    assertEquals( info.getComponent(), null );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertEquals( info.isActive(), true );
    assertEquals( info.isComputing(), false );

    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().get( 0 ).getName(), observer.getName() );
    assertUnmodifiable( info.getObservers() );

    assertEquals( info.getDependencies().size(), 1 );
    assertEquals( info.getDependencies().get( 0 ).getName(), observable.getName() );
    assertUnmodifiable( info.getDependencies() );

    assertEquals( info.getValue(), initialValue );

    assertEquals( info.isDisposed(), false );

    computedValue.dispose();

    assertEquals( info.isDisposed(), true );
  }

  @SuppressWarnings( "EqualsWithItself" )
  @Test
  public void equalsAndHashCode()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ComputedValue<Object> computedValue1 = context.createComputedValue( () -> "1" );
    final ComputedValue<Object> computedValue2 = context.createComputedValue( () -> "2" );

    final ComputedValueInfoImpl info1a = new ComputedValueInfoImpl( context.getSpy(), computedValue1 );
    final ComputedValueInfoImpl info1b = new ComputedValueInfoImpl( context.getSpy(), computedValue1 );
    final ComputedValueInfoImpl info2 = new ComputedValueInfoImpl( context.getSpy(), computedValue2 );

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
