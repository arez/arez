package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class ParametersDependency
{
  @ComponentDependency
  final @Nullable DisposeNotifier getTime( int i )
  {
    return null;
  }
}
