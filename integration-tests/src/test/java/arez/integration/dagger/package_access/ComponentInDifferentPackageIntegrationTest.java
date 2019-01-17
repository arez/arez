package arez.integration.dagger.package_access;

import arez.integration.AbstractArezIntegrationTest;
import arez.integration.dagger.package_access.other.TestComponent2DaggerComponentExtension;
import arez.integration.dagger.package_access.other.TestComponentDaggerComponentExtension;
import dagger.Component;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentInDifferentPackageIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Singleton
  @Component( modules = TestComponent2DaggerComponentExtension.DaggerModule.class )
  interface TestDaggerComponent
    extends TestComponentDaggerComponentExtension,
            TestComponent2DaggerComponentExtension
  {
  }

  @Test
  public void scenario()
  {
    // This test mostly is checking that the code correctly compiles by ensuring that all the artifacts
    // have the correct access level
    final TestDaggerComponent daggerComponent =
      DaggerComponentInDifferentPackageIntegrationTest_TestDaggerComponent.create();
    daggerComponent.bindTestComponent();

    // Expect to be a Provider<Object> rather than actual type as the type is not publicly accessible and
    // thus the dagger component can not reference it
    final TestComponentDaggerComponentExtension.DaggerSubcomponent subcomponent =
      daggerComponent.getTestComponentDaggerSubcomponent();
    final Provider<Object> rawProvider = subcomponent.createRawProvider();
    final Provider<?> provider = subcomponent.createProvider();
    assertEquals( provider, rawProvider );
  }
}
