package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;

@ArezComponent
public abstract class ObservablePairAnnotatedDependency
{
  @ComponentDependency
  abstract DisposeNotifier getValue();

  @Observable
  abstract void setValue( DisposeNotifier value );
}
