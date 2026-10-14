package com.example.misplaced_annotation;

import arez.annotations.ComponentRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ComponentRefOutsideArezTypeModel
{
  @ComponentRef
  @Nullable Object getComponent()
  {
    return null;
  }
}
