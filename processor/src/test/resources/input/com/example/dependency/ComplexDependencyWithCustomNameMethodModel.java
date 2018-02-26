package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import arez.annotations.Computed;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ComplexDependencyWithCustomNameMethodModel
{
  @Nonnull
  @ComponentNameRef
  abstract String getComponentName();

  @Dependency
  public Object getValue1()
  {
    return null;
  }

  @Computed
  @Dependency
  public Object getValue2()
  {
    return null;
  }

  @Dependency( action = Dependency.Action.SET_NULL )
  Object getValue3()
  {
    return null;
  }

  @Observable
  void setValue3( Object value )
  {
  }
}
