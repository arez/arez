package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;

@ArezComponent
public abstract class BadTypeDependency
{
  @Dependency
  public final Object getValue()
  {
    return null;
  }
}
