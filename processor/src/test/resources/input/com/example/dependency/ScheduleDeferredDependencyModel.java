package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.component.DisposeTrackable;

@ArezComponent( allowEmpty = true, deferSchedule = true )
public abstract class ScheduleDeferredDependencyModel
{
  @Dependency
  final DisposeTrackable getTime()
  {
    return null;
  }
}
