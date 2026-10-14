package com.example.misplaced_annotation;

import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class OnDepsChangeOutsideArezTypeModel
{
  @OnDepsChange
  void onDepsChange()
  {
  }
}
