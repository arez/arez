package com.example.dependency;

import arez.annotations.ArezComponent;
import arez.annotations.Dependency;

@ArezComponent( allowEmpty = true )
public abstract class TransitivelyDisposeTrackableDependencyModel
{
  @Dependency
  final MyDependentValue getTime()
  {
    return null;
  }
}
