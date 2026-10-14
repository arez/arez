package com.example.misplaced_annotation;

import arez.annotations.ComputableValueRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ComputableValueRefOutsideArezTypeModel
{
  @ComputableValueRef
  @Nullable Object getNameComputableValue()
  {
    return null;
  }
}
