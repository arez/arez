package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class TransitivelyDisposeTrackableDependencyModel
{
  @ComponentDependency
  MyDependentValue getTime()
  {
    return null;
  }
}
