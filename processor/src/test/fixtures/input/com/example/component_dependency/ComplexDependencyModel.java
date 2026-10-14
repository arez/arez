package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ComplexDependencyModel
{
  @ComponentDependency
  @Nullable DisposeNotifier getValue1()
  {
    return null;
  }

  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  @Nullable DisposeNotifier getValue3()
  {
    return null;
  }

  @Observable
  void setValue3( @Nullable DisposeNotifier value )
  {
  }
}
