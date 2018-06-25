package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.component.DisposeTrackable;

@ArezComponent( allowEmpty = true )
public abstract class SetNullBasicDependency
{
  @Dependency( action = Dependency.Action.SET_NULL )
  final DisposeTrackable getTime()
  {
    return null;
  }
}
