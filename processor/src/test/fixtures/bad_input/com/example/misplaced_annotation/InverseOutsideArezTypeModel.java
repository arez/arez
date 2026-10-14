package com.example.misplaced_annotation;

import arez.annotations.Inverse;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class InverseOutsideArezTypeModel
{
  @Inverse
  @Nullable Object getInverse()
  {
    return null;
  }
}
