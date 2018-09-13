package arez.integration.component_id_ref;

import arez.ArezTestUtil;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentIdRef;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentIdRefTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent
  {
    @ComponentIdRef
    abstract int getId();
  }

  @Test
  public void scenario()
    throws Throwable
  {
    final TestComponent component1 = new ComponentIdRefTest_Arez_TestComponent();
    final TestComponent component2 = new ComponentIdRefTest_Arez_TestComponent();

    assertNotEquals( component1.getId(), component2.getId() );
  }

  @Test
  public void scenarioIdForcedDespiteProductionMode()
    throws Throwable
  {
    ArezTestUtil.disableNativeComponents();
    ArezTestUtil.disableRegistries();
    ArezTestUtil.resetState();

    final TestComponent component1 = new ComponentIdRefTest_Arez_TestComponent();
    final TestComponent component2 = new ComponentIdRefTest_Arez_TestComponent();

    assertNotEquals( component1.getId(), component2.getId() );
  }
}
