package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;
import arez.annotations.Observable;
import arez.component.DisposeTrackable;

@ArezComponent
abstract class ComplexDependencyModel
{
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
