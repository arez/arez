package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class VoidReturnDependency
{
  @ComponentDependency
  void getTime()
  {
  }
}
