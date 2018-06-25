package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;

@ArezComponent( allowEmpty = true )
public abstract class ComplexDependencyModel
{
  @Dependency
  final Object getValue1()
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
