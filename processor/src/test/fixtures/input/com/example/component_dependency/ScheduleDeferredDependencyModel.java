package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;

@ArezComponent( deferSchedule = true )
abstract class ScheduleDeferredDependencyModel
{
  @ComponentDependency
  final DisposeNotifier getTime()
  {
    return null;
  }

  @ComponentDependency
  final DisposeNotifier dependency2 = null;
}
