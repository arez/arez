package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;

@ArezComponent
public abstract class ConcreteObservablePairWithInitializerDependency
{
  DisposeNotifier _value;

  ConcreteObservablePairWithInitializerDependency( final DisposeNotifier value )
  {
    _value = value;
  }

  @ComponentDependency
  DisposeNotifier getValue()
  {
    return _value;
  }

  @Observable
  void setValue( DisposeNotifier value )
  {
    _value = value;
  }
}
