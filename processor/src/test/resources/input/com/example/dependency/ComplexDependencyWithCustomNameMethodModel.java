package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ComplexDependencyWithCustomNameMethodModel
{
  @Nonnull
  @ComponentNameRef
  abstract String getComponentName();

  @Dependency
  final DisposeTrackable getValue1()
  {
    return null;
  }

  @Dependency( action = Dependency.Action.SET_NULL )
  DisposeTrackable getValue3()
  {
    return null;
  }

  @Observable
  void setValue3( DisposeTrackable value )
  {
  }
}
