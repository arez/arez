package com.example.misplaced_annotation;

import arez.annotations.ObservableValueRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ObservableValueRefOutsideArezTypeModel
{
  @ObservableValueRef
  @Nullable Object getNameObservableValue()
  {
    return null;
  }
}
