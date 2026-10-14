package com.example.misplaced_annotation;

import arez.annotations.ComponentNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ComponentNameRefOutsideArezTypeModel
{
  @ComponentNameRef
  @Nullable String getComponentName()
  {
    return "";
  }
}
