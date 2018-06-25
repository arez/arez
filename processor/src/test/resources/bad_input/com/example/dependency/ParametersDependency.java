package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.component.DisposeTrackable;

@ArezComponent( allowEmpty = true )
public abstract class ParametersDependency
{
  @Dependency
  final DisposeTrackable getTime( int i )
  {
    return null;
  }
}
