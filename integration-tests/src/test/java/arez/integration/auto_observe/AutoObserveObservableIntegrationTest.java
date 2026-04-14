package arez.integration.auto_observe;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class AutoObserveObservableIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final Child child1 = Child.create();
    final Owner owner = Owner.create();

    safeAction( () -> owner.setChild( child1 ) );

    assertFalse( Disposable.isDisposed( child1 ) );

    safeAction( () -> owner.setChild( null ) );

    assertTrue( Disposable.isDisposed( child1 ) );

    final Child child2 = Child.create();

    safeAction( () -> owner.setChild( child2 ) );

    assertFalse( Disposable.isDisposed( child2 ) );

    Disposable.dispose( owner );

    assertTrue( Disposable.isDisposed( child2 ) );
  }

  @ArezComponent( disposeOnDeactivate = true, allowEmpty = true )
  static abstract class Child
  {
    @Nonnull
    static Child create()
    {
      return new AutoObserveObservableIntegrationTest_Arez_Child();
    }
  }

  @ArezComponent
  static abstract class Owner
  {
    @Nonnull
    static Owner create()
    {
      return new AutoObserveObservableIntegrationTest_Arez_Owner();
    }

    @AutoObserve
    @Observable
    @Nullable
    abstract Child getChild();

    abstract void setChild( @Nullable Child child );
  }
}
