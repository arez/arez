
package arez.integration.sting.package_access.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.PostConstruct;

@ArezComponent( allowEmpty = true, sting = Feature.ENABLE )
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
