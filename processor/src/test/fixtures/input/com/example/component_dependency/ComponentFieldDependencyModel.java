package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ComponentFieldDependencyModel
{
  @NullMarked
  @ArezComponent( allowEmpty = true )
  abstract static class Foo
  {
  }

  @ComponentDependency
  final @Nullable Foo foo = null;
}
