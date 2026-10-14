package com.example.misplaced_annotation;

import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class MemoizeContextParameterOutsideArezTypeModel
{
  @MemoizeContextParameter
  void captureName( final @Nullable String name )
  {
  }
}
