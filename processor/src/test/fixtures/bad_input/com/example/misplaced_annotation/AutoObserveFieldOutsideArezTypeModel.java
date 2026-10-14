package com.example.misplaced_annotation;

import arez.annotations.AutoObserve;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
abstract class AutoObserveFieldOutsideArezTypeModel
{
  @AutoObserve
  final @Nullable Object dependency = null;
}
