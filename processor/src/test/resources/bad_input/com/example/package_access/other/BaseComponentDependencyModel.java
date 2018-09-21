package com.example.package_access.other;

import arez.annotations.ComponentDependency;

public abstract class BaseComponentDependencyModel
{
  @ComponentDependency
  Object getTime()
  {
    return null;
  }
}
