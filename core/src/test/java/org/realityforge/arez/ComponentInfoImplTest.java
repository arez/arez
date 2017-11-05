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

    final ComponentInfoImpl info = new ComponentInfoImpl( component );

    assertEquals( component.getContext(), context );
    assertEquals( info.getType(), type );
    assertEquals( info.getId(), id );
    assertEquals( info.getName(), name );
    assertEquals( info.toString(), name );

    assertEquals( info.getObservables().size(), 1 );
    assertEquals( info.getObservables().contains( observable ), true );
    assertUnmodifiable( info.getObservables() );

    assertEquals( info.getObservers().size(), 1 );
    assertEquals( info.getObservers().contains( observer ), true );
    assertUnmodifiable( info.getObservers() );

    assertEquals( info.getComputedValues().size(), 1 );
    assertEquals( info.getComputedValues().contains( computedValue ), true );
    assertUnmodifiable( info.getComputedValues() );

    assertFalse( Disposable.isDisposed( info ) );
    assertFalse( Disposable.isDisposed( component ) );
    info.dispose();
    assertTrue( Disposable.isDisposed( info ) );
    assertTrue( Disposable.isDisposed( component ) );
  }

  private <T> void assertUnmodifiable( @Nonnull final Collection<T> collection )
  {
    assertThrows( UnsupportedOperationException.class, () -> collection.remove( collection.iterator().next() ) );
  }
}
