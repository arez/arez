package arez;

import arez.spy.ObserverInfo;
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
  public void isReadOnly_on_READ_WRITE_observer()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observableValue = context.observable();
    final Observer observer = context.observer( observableValue::reportObserved, Options.READ_WRITE );

    assertEquals( observer.asInfo().isReadOnly(), false );
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
