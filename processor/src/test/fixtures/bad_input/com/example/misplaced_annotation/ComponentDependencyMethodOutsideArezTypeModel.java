package com.example.misplaced_annotation;

import arez.annotations.ComponentDependency;

final class ComponentDependencyMethodOutsideArezTypeModel
{
  @ComponentDependency
  Object getDependency()
  {
    return null;
  }
}
