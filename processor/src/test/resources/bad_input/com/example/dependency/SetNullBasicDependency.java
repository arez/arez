package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;

@ArezComponent( allowEmpty = true )
public abstract class SetNullBasicDependency
{
  @Dependency( action = Dependency.Action.SET_NULL )
  Object getTime()
  {
    return null;
  }
}
