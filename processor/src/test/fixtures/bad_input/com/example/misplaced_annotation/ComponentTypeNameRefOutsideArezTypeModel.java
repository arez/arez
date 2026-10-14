package com.example.misplaced_annotation;

import arez.annotations.ComponentTypeNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ComponentTypeNameRefOutsideArezTypeModel
{
  @ComponentTypeNameRef
  @Nullable String getComponentTypeName()
  {
    return "";
  }
}
