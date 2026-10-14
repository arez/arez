package com.example.misplaced_annotation;

import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class CascadeDisposeFieldOutsideArezTypeModel
{
  @CascadeDispose
  final @Nullable Object _dependency = null;
}
