package arez.integration.component_dependency;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentDependencyRuntimeValidationIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final Model1 model1a1 = Model1.create();
    final Model2 model2a = Model2.create( model1a1 );

    assertFalse( Disposable.isDisposed( model1a1 ) );
    assertFalse( Disposable.isDisposed( model2a ) );

    Disposable.dispose( model1a1 );

    assertTrue( Disposable.isDisposed( model1a1 ) );
    assertTrue( Disposable.isDisposed( model2a ) );
  }

  @Test
  public void scenario_withBadDependency()
  {
    final BadType value = new BadType();
    final IllegalStateException exception = expectThrows( IllegalStateException.class, () -> Model2.create( value ) );
    assertEquals( exception.getMessage(),
                  "Arez-0178: Object passed to asDisposeNotifier does not implement " +
                  "DisposeNotifier. Object: " + value );
  }

  interface MyInterface
  {
  }

  private static class BadType
    implements MyInterface
  {
  }

  @ArezComponent
  static abstract class Model1
    implements MyInterface
  {
    static Model1 create()
    {
      return new ComponentDependencyRuntimeValidationIntegrationTest_Arez_Model1();
    }

    @Observable
    abstract String getName();

    abstract void setName( String name );
  }

  @ArezComponent
  static abstract class Model2
  {
    @ComponentDependency( validateTypeAtRuntime = true )
    final MyInterface _reference1;

    static Model2 create( final MyInterface reference1 )
    {
      return new ComponentDependencyRuntimeValidationIntegrationTest_Arez_Model2( reference1 );
    }

    Model2( final MyInterface reference1 )
    {
      _reference1 = reference1;
    }
  }
}
