package arez.integration.component_observable;

import arez.Arez;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import arez.spy.ObservableInfo;
import java.util.List;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentObservableTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent
  {
  }

  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent2
  {
  }

  @Test
  public void scenario()
    throws Throwable
  {
    final ComponentObservableTest_Arez_TestComponent component = new ComponentObservableTest_Arez_TestComponent();
    // The base class will verify that there are no observer errors triggered in autorun
    final Observer observer = Arez.context().autorun( () -> ComponentObservable.observe( component ) );
    final List<ObservableInfo> dependencies = Arez.context().getSpy().getDependencies( observer );
    assertEquals( dependencies.size(), 1 );
    assertEquals( dependencies.get( 0 ).getName(), "TestComponent.0.isDisposed" );
  }

  @Test
  public void outsideTransaction()
    throws Throwable
  {
    final ComponentObservableTest_Arez_TestComponent2 component = new ComponentObservableTest_Arez_TestComponent2();
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> ComponentObservable.observe( component ) );
    assertEquals( exception.getMessage(),
                  "Arez-0117: Attempting to get current transaction but no transaction is active." );
  }
}
