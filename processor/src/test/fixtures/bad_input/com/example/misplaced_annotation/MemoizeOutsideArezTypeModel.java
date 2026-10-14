package com.example.misplaced_annotation;

import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class MemoizeOutsideArezTypeModel
{
  @Memoize
  @Nullable String getName()
  {
    return "";
  }
}
