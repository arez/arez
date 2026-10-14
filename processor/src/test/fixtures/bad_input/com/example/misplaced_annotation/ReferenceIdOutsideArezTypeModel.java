package com.example.misplaced_annotation;

import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class ReferenceIdOutsideArezTypeModel
{
  @ReferenceId
  @Nullable Object getReferenceId()
  {
    return null;
  }
}
