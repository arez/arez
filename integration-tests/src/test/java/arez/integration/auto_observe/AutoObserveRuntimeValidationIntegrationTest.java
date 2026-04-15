package arez.integration.auto_observe;

import arez.Disposable;
import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class AutoObserveRuntimeValidationIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final Child child = Child.create();
    final Owner owner = Owner.create( child );

    assertFalse( Disposable.isDisposed( child ) );

    Disposable.dispose( owner );

    assertTrue( Disposable.isDisposed( child ) );
  }

  @Test
  public void scenario_withBadDependency()
  {
    captureObserverErrors();
    final BadType value = new BadType();
    assertInvariant( () -> Owner.create( value ),
                     "Field annotated with @AutoObserve( validateTypeAtRuntime = true ) references a " +
                     "non-null value that does not implement ComponentObservable. Value: " + value );
  }

  @Test
  public void scenario_withNullDependency()
  {
    captureObserverErrors();
    final Owner owner = Owner.create( null );

    assertNotNull( owner );
    assertEquals( getObserverErrors().size(), 0 );

    Disposable.dispose( owner );
  }

  @ArezComponentLike
  interface MyType
  {
  }

  private static final class BadType
    implements MyType
  {
  }

  @ArezComponent( disposeOnDeactivate = true, allowEmpty = true )
  static abstract class Child
    implements MyType
  {
    @Nonnull
    static Child create()
    {
      return new AutoObserveRuntimeValidationIntegrationTest_Arez_Child();
    }
  }

  @ArezComponent
  static abstract class Owner
  {
    @AutoObserve( validateTypeAtRuntime = true )
    final MyType _child;

    @Nonnull
    static Owner create( final MyType child )
    {
      return new AutoObserveRuntimeValidationIntegrationTest_Arez_Owner( child );
    }

    Owner( final MyType child )
    {
      _child = child;
    }
  }
}
