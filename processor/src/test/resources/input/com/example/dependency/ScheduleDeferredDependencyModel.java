package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;

@ArezComponent( allowEmpty = true, deferSchedule = true )
public abstract class ScheduleDeferredDependencyModel
{
  @Dependency
  public Object getTime()
  {
    return null;
  }
}
