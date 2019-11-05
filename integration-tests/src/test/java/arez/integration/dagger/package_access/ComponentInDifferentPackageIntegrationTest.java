package arez.integration.dagger.package_access;

import arez.integration.AbstractArezIntegrationTest;
import arez.integration.dagger.package_access.other.TestComponent2;
import arez.integration.dagger.package_access.other.TestComponent2DaggerModule;
import dagger.Component;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentInDifferentPackageIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Singleton
  @Component( modules = TestComponent2DaggerModule.class )
  interface TestDaggerComponent
  {
    TestComponent2 getTestComponent2();
  }

  @Test
  public void scenario()
  {
    // This test mostly is checking that the code correctly compiles by ensuring that all the artifacts
    // have the correct access level
    final TestDaggerComponent daggerComponent =
      DaggerComponentInDifferentPackageIntegrationTest_TestDaggerComponent.create();

    assertNotNull( daggerComponent.getTestComponent2() );
  }
}
