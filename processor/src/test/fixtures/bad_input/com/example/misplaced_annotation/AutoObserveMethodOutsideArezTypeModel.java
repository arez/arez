package com.example.misplaced_annotation;

import arez.annotations.AutoObserve;

final class AutoObserveMethodOutsideArezTypeModel
{
  @AutoObserve
  Object getDependency()
  {
    return null;
  }
}
