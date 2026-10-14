package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MultiComponentDependencyModel
{
  @NullMarked
  @ArezComponent( allowEmpty = true )
  abstract static class Foo
  {
  }

  @ComponentDependency
  @Nullable Foo getFoo()
  {
    return null;
  }

  @ComponentDependency
  @Nullable Foo getFoo2()
  {
    return null;
  }

  @ComponentDependency
  @Nullable Foo getFoo3()
  {
    return null;
  }

  @Observable
  abstract void setFoo4( @Nullable Foo foo );

  @ComponentDependency
  abstract @Nullable Foo getFoo4();

  abstract void setFoo5( @Nullable Foo foo );

  @ComponentDependency
  @Observable
  abstract @Nullable Foo getFoo5();
}
