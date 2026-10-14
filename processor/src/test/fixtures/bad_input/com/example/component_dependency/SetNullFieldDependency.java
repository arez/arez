package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.component.DisposeNotifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class SetNullFieldDependency
{
  @ComponentDependency( action = ComponentDependency.Action.SET_NULL )
  final @Nullable DisposeNotifier time = null;
}
