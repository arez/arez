package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;

@ArezComponent
abstract class AbstractObservableDependency
{
  @Observable
  @ComponentDependency
  abstract DisposeNotifier getValue();

  abstract void setValue( DisposeNotifier value );
}
