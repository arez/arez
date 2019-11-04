package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;

@ArezComponent
public abstract class ObservableDependency
{
  @Observable
  @ComponentDependency
  DisposeNotifier getValue()
  {
    return null;
  }

  void setValue( DisposeNotifier value )
  {
  }
}
