package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;

@ArezComponent
public abstract class ConcreteObservablePairWithInitializerDependency
{
  DisposeTrackable _value;

  ConcreteObservablePairWithInitializerDependency( final DisposeTrackable value )
  {
    _value = value;
  }

  @ComponentDependency
  DisposeTrackable getValue()
  {
    return _value;
  }

  @Observable
  void setValue( DisposeTrackable value )
  {
    _value = value;
  }
}
