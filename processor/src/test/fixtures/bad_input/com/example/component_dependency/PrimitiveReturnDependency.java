package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent( allowEmpty = true )
public abstract class PrimitiveReturnDependency
{
  @ComponentDependency
  final int getTime()
  {
    return 0;
  }
}
