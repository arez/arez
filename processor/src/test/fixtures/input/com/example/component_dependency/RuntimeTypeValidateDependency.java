package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ArezComponentLike;
import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class RuntimeTypeValidateDependency
{
  @ComponentDependency( validateTypeAtRuntime = true )
  public @Nullable MyType getValue()
  {
    return null;
  }
  @NullMarked
  @ArezComponentLike
  interface MyType
  {
  }
}
