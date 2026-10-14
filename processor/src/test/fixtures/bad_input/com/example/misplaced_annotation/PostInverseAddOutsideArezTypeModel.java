package com.example.misplaced_annotation;

import arez.annotations.PostInverseAdd;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class PostInverseAddOutsideArezTypeModel
{
  @PostInverseAdd
  void postNameAdd( final @Nullable Object value )
  {
  }
}
