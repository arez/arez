package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;

@ArezComponent( allowEmpty = true )
public abstract class PrivateDependency
{
  @Dependency
  private Object getTime()
  {
    return null;
  }
}
