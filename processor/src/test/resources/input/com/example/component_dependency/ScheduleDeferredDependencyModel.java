package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeTrackable;

@ArezComponent( deferSchedule = true )
abstract class ScheduleDeferredDependencyModel
{
  @ComponentDependency
  final DisposeTrackable getTime()
  {
    return null;
  }

  @ComponentDependency
  final DisposeTrackable dependency2 = null;
}
