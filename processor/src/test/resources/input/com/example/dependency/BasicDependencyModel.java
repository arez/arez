package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;

@ArezComponent( allowEmpty = true )
public abstract class BasicDependencyModel
{
  @Dependency
  public Object getTime()
  {
    return null;
  }
}
