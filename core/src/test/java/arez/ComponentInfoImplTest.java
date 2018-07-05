package arez;

import java.util.Collection;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentInfoImplTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String type = ValueUtil.randomString();
    final String id = ValueUtil.randomString();
    final String name = ValueUtil.randomString();
    final Observer observer = context.autorun( AbstractArezTest::observeADependency );
    final Observable observable = context.createObservable();
    final ComputedValue computedValue = context.computedValue( () -> "" );

    final Component component = context.component( type, id, name );
    component.addObserver( observer );
    component.addObservable( observable );
    component.addComputedValue( computedValue );

    final ComponentInfoImpl info = new ComponentInfoImpl( context.getSpy(), component );

    assertEquals( info.getType(), type );
    assertEquals( info.getId(), id );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertEquals( info.getObservables().size(), 1 );
    assertEquals( info.getObservables().get( 0 ).getName(), observable.getName() );
    assertUnmodifiable( info.getObservables() );

    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().get( 0 ).getName(), observer.getName() );
    assertUnmodifiable( info.getObservers() );

    assertEquals( info.getComputedValues().size(), 1 );
    assertEquals( info.getComputedValues().get( 0 ).getName(), computedValue.getName() );
    assertUnmodifiable( info.getComputedValues() );

    assertFalse( info.isDisposed() );
    component.dispose();
    assertTrue( info.isDisposed() );
  }

  @SuppressWarnings( "EqualsWithItself" )
  @Test
  public void equalsAndHashCode()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Component component1 = context.component( ValueUtil.randomString(), ValueUtil.randomString() );
    final Component component2 = context.component( ValueUtil.randomString(), ValueUtil.randomString() );

    final ComponentInfoImpl info1a = new ComponentInfoImpl( context.getSpy(), component1 );
    final ComponentInfoImpl info1b = new ComponentInfoImpl( context.getSpy(), component1 );
    final ComponentInfoImpl info2 = new ComponentInfoImpl( context.getSpy(), component2 );

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

    assertEquals( info1a.hashCode(), component1.hashCode() );
    assertEquals( info1a.hashCode(), info1b.hashCode() );
    assertEquals( info2.hashCode(), component2.hashCode() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection )
  {
    assertThrows( UnsupportedOperationException.class, () -> collection.remove( collection.iterator().next() ) );
  }
}
