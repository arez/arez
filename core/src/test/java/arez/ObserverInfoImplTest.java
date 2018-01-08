package arez;

import java.util.Collection;
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
    final Observable<Object> observable = context.createObservable();
    final Observer observer = context.autorun( name, false, observable::reportObserved );

    final ObserverInfoImpl info = new ObserverInfoImpl( context.getSpy(), observer );

    assertEquals( info.getComponent(), null );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertEquals( info.getDependencies().size(), 1 );
    assertEquals( info.getDependencies().get( 0 ).getName(), observable.getName() );
    assertUnmodifiable( info.getDependencies() );

    assertEquals( info.isComputedValue(), false );
    assertEquals( info.isReadOnly(), true );
    assertEquals( info.isRunning(), false );
    assertEquals( info.isScheduled(), false );
    assertEquals( info.isDisposed(), false );

    observer.dispose();

    assertEquals( info.isDisposed(), true );
  }

  @Test
  public void asComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final ComputedValue<String> computedValue = context.createComputedValue( name, () -> "" );

    final Observer observer = computedValue.getObserver();

    final ObserverInfoImpl info = new ObserverInfoImpl( context.getSpy(), observer );

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
    final Observable<Object> observable = context.createObservable();
    final Observer observer1 = context.autorun( ValueUtil.randomString(), false, observable::reportObserved );
    final Observer observer2 = context.autorun( ValueUtil.randomString(), false, observable::reportObserved );

    final ObserverInfoImpl info1a = new ObserverInfoImpl( context.getSpy(), observer1 );
    final ObserverInfoImpl info1b = new ObserverInfoImpl( context.getSpy(), observer1 );
    final ObserverInfoImpl info2 = new ObserverInfoImpl( context.getSpy(), observer2 );

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
