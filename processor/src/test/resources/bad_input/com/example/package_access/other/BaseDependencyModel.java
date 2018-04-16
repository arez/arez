package com.example.package_access.other;

import arez.annotations.Dependency;

public abstract class BaseDependencyModel
{
  @Dependency
  Object getTime()
  {
    return null;
  }
}
