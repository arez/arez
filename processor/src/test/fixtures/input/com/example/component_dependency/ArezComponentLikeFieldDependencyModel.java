package com.example.component_dependency;

import arez.annotations.ArezComponentLike;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent
abstract class ArezComponentLikeFieldDependencyModel
{
  @ComponentDependency
  final MyType time = null;

  @ArezComponentLike
  public interface MyType
  {
  }
}
