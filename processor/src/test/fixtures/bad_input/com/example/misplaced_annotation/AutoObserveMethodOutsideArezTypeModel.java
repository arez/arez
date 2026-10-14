package com.example.misplaced_annotation;

import arez.annotations.AutoObserve;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class AutoObserveMethodOutsideArezTypeModel
{
  @AutoObserve
  @Nullable Object getDependency()
  {
    return null;
  }
}
