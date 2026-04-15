package com.example.component_dependency;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class ArezComponentLikeMethodDependencyModel
{
  @ComponentDependency
  public MyType getTime()
  {
    return null;
  }

  @ArezComponentLike
  public interface MyType
  {
  }
}
