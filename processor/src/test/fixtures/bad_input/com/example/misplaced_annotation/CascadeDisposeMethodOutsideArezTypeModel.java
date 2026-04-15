package com.example.misplaced_annotation;

import arez.annotations.CascadeDispose;

final class CascadeDisposeMethodOutsideArezTypeModel
{
  @CascadeDispose
  Object getDependency()
  {
    return null;
  }
}
