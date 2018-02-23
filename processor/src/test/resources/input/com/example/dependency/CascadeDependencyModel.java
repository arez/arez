package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;

@ArezComponent( allowEmpty = true )
public abstract class CascadeDependencyModel
{
  @Dependency( action = Dependency.Action.CASCADE )
  public Object getTime()
  {
    return null;
  }
}
