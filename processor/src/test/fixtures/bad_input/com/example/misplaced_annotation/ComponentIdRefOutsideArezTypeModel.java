package com.example.misplaced_annotation;

import arez.annotations.ComponentIdRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ComponentIdRefOutsideArezTypeModel
{
  @ComponentIdRef
  @Nullable Object getIdObservable()
  {
    return null;
  }
}
