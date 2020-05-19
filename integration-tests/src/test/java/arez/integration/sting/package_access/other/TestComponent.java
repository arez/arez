
package arez.integration.sting.package_access.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.PostConstruct;

@ArezComponent( sting = Feature.ENABLE )
public abstract class TestComponent
{
  TestComponent( final MyDependency myDependency )
  {
  }

  @PostConstruct
  void postConstruct()
  {
  }

  @Observable
  abstract String getValue();

  abstract void setValue( String value );
}
