package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Observable;

@ArezComponent
abstract class MultiComponentDependencyModel
{
  @ArezComponent( allowEmpty = true )
  abstract static class Foo
  {
  }

  @ComponentDependency
  Foo getFoo()
  {
    return null;
  }

  @ComponentDependency
  Foo getFoo2()
  {
    return null;
  }

  @ComponentDependency
  Foo getFoo3()
  {
    return null;
  }

  @Observable
  abstract void setFoo4( Foo foo );

  @ComponentDependency
  abstract Foo getFoo4();

  abstract void setFoo5( Foo foo );

  @ComponentDependency
  @Observable
  abstract Foo getFoo5();
}
