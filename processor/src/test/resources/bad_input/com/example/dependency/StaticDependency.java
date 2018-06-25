package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.component.DisposeTrackable;

@ArezComponent( allowEmpty = true )
public abstract class StaticDependency
{
  @Dependency
  static DisposeTrackable getTime()
  {
    return null;
  }
}
