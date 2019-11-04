package com.example.component_dependency;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;

@ArezComponent( allowEmpty = true )
public abstract class VoidReturnDependency
{
  @ComponentDependency
  void getTime()
  {
  }
}
