
package arez.integration.dagger.package_access.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.Observable;
import arez.annotations.PostConstruct;

@ArezComponent( dagger = Feature.ENABLE, inject = InjectMode.CONSUME )
abstract class TestComponent
{
  TestComponent( final MyDependency myDependency )
  {
  }

  @PostConstruct
  final void postConstruct()
  {
  }

  @Observable
  abstract String getValue();

  abstract void setValue( String value );
}
