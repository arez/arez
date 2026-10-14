package com.example.misplaced_annotation;

import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class PostDisposeOutsideArezTypeModel
{
  @PostDispose
  void cleanup()
  {
  }
}
