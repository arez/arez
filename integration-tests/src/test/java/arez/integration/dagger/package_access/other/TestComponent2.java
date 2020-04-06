
package arez.integration.dagger.package_access.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.PostConstruct;
import javax.inject.Singleton;

@Singleton
@ArezComponent( allowEmpty = true, sting = Feature.DISABLE)
public abstract class TestComponent2
{
  TestComponent2( final MyDependency myDependency, final MyDependency2 myOtherDependency )
  {
  }

  @PostConstruct
  final void postConstruct()
  {
  }
}
