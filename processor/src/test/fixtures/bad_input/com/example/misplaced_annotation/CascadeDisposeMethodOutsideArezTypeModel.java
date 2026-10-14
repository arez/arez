package com.example.misplaced_annotation;

import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class CascadeDisposeMethodOutsideArezTypeModel
{
  @CascadeDispose
  @Nullable Object getDependency()
  {
    return null;
  }
}
