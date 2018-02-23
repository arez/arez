package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;

@ArezComponent
public abstract class ObservableDependency
{
  @Observable
  @Dependency
  Object getValue()
  {
    return null;
  }

  void setValue( Object value )
  {
  }
}
