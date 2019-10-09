package arez;

import arez.spy.ComponentInfo;
import java.util.Collection;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentInfoImplTest
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

  @SuppressWarnings( "EqualsWithItself" )
  @Test
  public void equalsAndHashCode()
  {
    final ArezContext context = Arez.context();

    final Component component1 = context.component( ValueUtil.randomString(), ValueUtil.randomString() );
    final Component component2 = context.component( ValueUtil.randomString(), ValueUtil.randomString() );

    final ComponentInfo info1a = component1.asInfo();
    final ComponentInfo info1b = new ComponentInfoImpl( component1 );
    final ComponentInfo info2 = component2.asInfo();

    //noinspection EqualsBetweenInconvertibleTypes
    assertFalse( info1a.equals( "" ) );

    assertTrue( info1a.equals( info1a ) );
    assertTrue( info1a.equals( info1b ) );
    assertFalse( info1a.equals( info2 ) );

    assertTrue( info1b.equals( info1a ) );
    assertTrue( info1b.equals( info1b ) );
    assertFalse( info1b.equals( info2 ) );

    assertFalse( info2.equals( info1a ) );
    assertFalse( info2.equals( info1b ) );
    assertTrue( info2.equals( info2 ) );

    assertEquals( info1a.hashCode(), component1.hashCode() );
    assertEquals( info1a.hashCode(), info1b.hashCode() );
    assertEquals( info2.hashCode(), component2.hashCode() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection )
  {
    assertThrows( UnsupportedOperationException.class, () -> collection.remove( collection.iterator().next() ) );
  }
}
