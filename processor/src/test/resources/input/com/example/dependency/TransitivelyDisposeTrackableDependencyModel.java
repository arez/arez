package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;

@ArezComponent
public abstract class TransitivelyDisposeTrackableDependencyModel
{
  @Dependency
  final MyDependentValue getTime()
  {
    return null;
  }
}
