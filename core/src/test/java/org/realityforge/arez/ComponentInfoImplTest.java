package org.realityforge.arez;

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
    final Observer observer = context.autorun( () -> {
    } );
    final Observable observable = context.createObservable();
    final ComputedValue computedValue = context.createComputedValue( () -> "" );

    final Component component = context.createComponent( type, id, name );
    component.addObserver( observer );
    component.addObservable( observable );
    component.addComputedValue( computedValue );

    final ComponentInfoImpl info = new ComponentInfoImpl( context.getSpy(), component );

    assertEquals( info.getType(), type );
    assertEquals( info.getId(), id );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertEquals( info.getObservables().size(), 1 );
    assertEquals( info.getObservables().contains( observable ), true );
    assertUnmodifiable( info.getObservables() );

    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().iterator().next().getName(), observer.getName() );
    assertUnmodifiable( info.getObservers() );

    assertEquals( info.getComputedValues().size(), 1 );
    assertEquals( info.getComputedValues().contains( computedValue ), true );
    assertUnmodifiable( info.getComputedValues() );

    assertFalse( info.isDisposed() );
    component.dispose();
    assertTrue( info.isDisposed() );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection )
  {
    assertThrows( UnsupportedOperationException.class, () -> collection.remove( collection.iterator().next() ) );
  }
}
