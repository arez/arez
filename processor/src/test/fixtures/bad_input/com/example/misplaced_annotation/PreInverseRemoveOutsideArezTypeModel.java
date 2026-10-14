package com.example.misplaced_annotation;

import arez.annotations.PreInverseRemove;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class PreInverseRemoveOutsideArezTypeModel
{
  @PreInverseRemove
  void preNameRemove( final @Nullable Object value )
  {
  }
}
