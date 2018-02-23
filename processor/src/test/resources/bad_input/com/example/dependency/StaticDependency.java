package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;

@ArezComponent( allowEmpty = true )
public abstract class StaticDependency
{
  @Dependency
  static Object getTime()
  {
    return null;
  }
}
