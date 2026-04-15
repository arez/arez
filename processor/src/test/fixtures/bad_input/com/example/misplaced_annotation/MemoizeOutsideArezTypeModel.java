package com.example.misplaced_annotation;

import arez.annotations.Memoize;

final class MemoizeOutsideArezTypeModel
{
  @Memoize
  String getName()
  {
    return "";
  }
}
