package com.example.auto_observe;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ComponentDependencyAndAutoObserveModel
{
  @ComponentDependency( validateTypeAtRuntime = true )
  @AutoObserve( validateTypeAtRuntime = true )
  final @Nullable MyType _field = null;

  @NullMarked
  @ArezComponentLike
  interface MyType
  {
  }
}
