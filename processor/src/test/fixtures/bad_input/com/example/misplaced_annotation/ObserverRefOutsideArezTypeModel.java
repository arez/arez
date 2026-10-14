package com.example.misplaced_annotation;

import arez.annotations.ObserverRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ObserverRefOutsideArezTypeModel
{
  @ObserverRef
  @Nullable Object getObserver()
  {
    return null;
  }
}
