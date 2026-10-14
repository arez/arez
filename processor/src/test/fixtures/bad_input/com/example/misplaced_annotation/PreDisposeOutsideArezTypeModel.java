package com.example.misplaced_annotation;

import arez.annotations.PreDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PreDisposeOutsideArezTypeModel
{
  @PreDispose
  void prepareForDispose()
  {
  }
}
