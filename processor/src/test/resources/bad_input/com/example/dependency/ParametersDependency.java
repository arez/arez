package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;

@ArezComponent( allowEmpty = true )
public abstract class ParametersDependency
{
  @Dependency
  Object getTime( int i )
  {
    return null;
  }
}
