package com.example.component_dependency;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ArezComponentLikeFieldDependencyModel
{
  @ComponentDependency
  final @Nullable MyType time = null;

  @NullMarked
  @ArezComponentLike
  public interface MyType
  {
  }
}
