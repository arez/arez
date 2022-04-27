package arez;

import arez.spy.ComponentInfo;
import java.util.Collection;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComponentInfoImplTest
  extends AbstractTest
{
  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();
    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();
    final String name = ValueUtil.randomString();
    final Observer observer = context.observer( AbstractTest::observeADependency );
    final ObservableValue<?> observableValue = context.observable();
    final ComputableValue<?> computableValue = context.computable( () -> "" );

    final Component component = context.component( type, id, name );
    component.addObserver( observer );
    component.addObservableValue( observableValue );
    component.addComputableValue( computableValue );

    final ComponentInfo info = component.asInfo();

    assertEquals( info.getType(), type );
    assertEquals( info.getId(), id );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertEquals( info.getObservableValues().size(), 1 );
    assertEquals( info.getObservableValues().get( 0 ).getName(), observableValue.getName() );
    assertUnmodifiable( info.getObservableValues() );

    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().get( 0 ).getName(), observer.getName() );
    assertUnmodifiable( info.getObservers() );

    assertEquals( info.getComputableValues().size(), 1 );
    assertEquals( info.getComputableValues().get( 0 ).getName(), computableValue.getName() );
    assertUnmodifiable( info.getComputableValues() );

    assertFalse( info.isDisposed() );
    component.dispose();
    assertTrue( info.isDisposed() );
  }

  @Test
  public void equalsAndHashCode()
  {
    final ArezContext context = Arez.context();

    final Component component1 = context.component( ValueUtil.randomString(), ValueUtil.randomString() );
    final Component component2 = context.component( ValueUtil.randomString(), ValueUtil.randomString() );

    final ComponentInfo info1a = component1.asInfo();
    final ComponentInfo info1b = new ComponentInfoImpl( component1 );
    final ComponentInfo info2 = component2.asInfo();

    //noinspection AssertBetweenInconvertibleTypes
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

    assertEquals( info1a.hashCode(), component1.hashCode() );
    assertEquals( info1a.hashCode(), info1b.hashCode() );
    assertEquals( info2.hashCode(), component2.hashCode() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection )
  {
    assertThrows( UnsupportedOperationException.class, () -> collection.remove( collection.iterator().next() ) );
  }
}
