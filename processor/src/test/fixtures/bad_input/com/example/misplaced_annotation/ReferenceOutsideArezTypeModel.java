package com.example.misplaced_annotation;

import arez.annotations.Reference;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ReferenceOutsideArezTypeModel
{
  @Reference
  @Nullable Object getReference()
  {
    return null;
  }
}
