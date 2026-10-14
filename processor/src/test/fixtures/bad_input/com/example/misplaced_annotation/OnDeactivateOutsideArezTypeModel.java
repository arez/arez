package com.example.misplaced_annotation;

import arez.annotations.OnDeactivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class OnDeactivateOutsideArezTypeModel
{
  @OnDeactivate
  void onDeactivate()
  {
  }
}
