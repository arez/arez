package com.example.misplaced_annotation;

import arez.annotations.PostDispose;

final class PostDisposeOutsideArezTypeModel
{
  @PostDispose
  void cleanup()
  {
  }
}
