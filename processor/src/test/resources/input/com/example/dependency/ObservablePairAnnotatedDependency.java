package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;

@ArezComponent
public abstract class ObservablePairAnnotatedDependency
{
  @Dependency
  abstract Object getValue();

  @Observable
  abstract void setValue( Object value );
}
