package com.example.misplaced_annotation;

import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class ObserveOutsideArezTypeModel
{
  @Observe
  void observeState()
  {
  }
}
