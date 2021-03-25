package arez.integration.component_observable;

import arez.Arez;
import arez.ArezContext;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.component.ComponentObservable;
import arez.integration.AbstractArezIntegrationTest;
import arez.spy.ObservableValueInfo;
import java.util.List;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComponentObservableTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( allowEmpty = true, observable = Feature.ENABLE )
  static abstract class TestComponent
  {
  }

  @ArezComponent( allowEmpty = true, observable = Feature.ENABLE )
  static abstract class TestComponent2
  {
  }

  @Test
  public void scenario()
  {
    final ArezContext context = Arez.context();
    final ComponentObservableTest_Arez_TestComponent component = new ComponentObservableTest_Arez_TestComponent();
    // The base class will verify that there are no observer errors triggered in observed
    final Observer observer = context.observer( () -> ComponentObservable.observe( component ) );
    final List<ObservableValueInfo> dependencies = context.getSpy().asObserverInfo( observer ).getDependencies();
    assertEquals( dependencies.size(), 1 );
    assertEquals( dependencies.get( 0 ).getName(), "arez_integration_component_observable_ComponentObservableTest_TestComponent.1.isDisposed" );
  }

  @Test
  public void outsideTransaction()
  {
    final ComponentObservableTest_Arez_TestComponent2 component = new ComponentObservableTest_Arez_TestComponent2();
    assertInvariant( () -> ComponentObservable.observe( component ),
                     "Arez-0117: Attempting to get current transaction but no transaction is active." );
  }
}
