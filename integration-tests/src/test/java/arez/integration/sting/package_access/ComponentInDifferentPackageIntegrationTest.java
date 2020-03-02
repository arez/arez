package arez.integration.sting.package_access;

import arez.integration.AbstractArezIntegrationTest;
import arez.integration.sting.package_access.other.TestComponent2;
import arez.integration.sting.package_access.other.TestComponent2Fragment;
import org.testng.annotations.Test;
import sting.Injector;
import static org.testng.Assert.*;

public class ComponentInDifferentPackageIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Injector( includes = TestComponent2Fragment.class )
  interface MyInjector
  {
    TestComponent2 getTestComponent2();
  }

  @Test
  public void scenario()
  {
    // This test mostly is checking that the code correctly compiles by ensuring that all the artifacts
    // have the correct access level
    final MyInjector injector = new ComponentInDifferentPackageIntegrationTest_Sting_MyInjector();

    assertNotNull( injector.getTestComponent2() );
  }
}
