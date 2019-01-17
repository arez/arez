
package arez.integration.dagger.package_access.other;

import arez.annotations.ArezComponent;
import arez.annotations.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ArezComponent( allowEmpty = true )
public abstract class TestComponent2
{
  @Inject
  MyDependency2 _myOtherDependency;

  TestComponent2( final MyDependency myDependency )
  {
  }

  @PostConstruct
  final void postConstruct()
  {
  }
}
