package arez.integration.auto_observe;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class AutoObserveFieldIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final Child child = Child.create();
    final Owner owner = Owner.create( child );

    assertFalse( Disposable.isDisposed( child ) );
    assertFalse( Disposable.isDisposed( owner ) );

    Disposable.dispose( owner );

    assertTrue( Disposable.isDisposed( owner ) );
    assertTrue( Disposable.isDisposed( child ) );
  }

  @ArezComponent( disposeOnDeactivate = true, allowEmpty = true )
  static abstract class Child
  {
    @Nonnull
    static Child create()
    {
      return new AutoObserveFieldIntegrationTest_Arez_Child();
    }
  }

  @ArezComponent
  static abstract class Owner
  {
    @AutoObserve
    @Nonnull
    final Child _child;

    @Nonnull
    static Owner create( @Nonnull final Child child )
    {
      return new AutoObserveFieldIntegrationTest_Arez_Owner( child );
    }

    Owner( @Nonnull final Child child )
    {
      _child = child;
    }
  }
}
