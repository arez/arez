package com.example.misplaced_annotation;

import arez.annotations.ContextRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ContextRefOutsideArezTypeModel
{
  @ContextRef
  @Nullable Object getContext()
  {
    return null;
  }
}
