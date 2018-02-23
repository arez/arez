package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;

@ArezComponent
public abstract class AbstractObservableDependency
{
  @Observable
  @Dependency
  abstract Object getValue();

  abstract void setValue( Object value );
}
