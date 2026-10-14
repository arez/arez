package com.example.misplaced_annotation;

import arez.annotations.OnActivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class OnActivateOutsideArezTypeModel
{
  @OnActivate
  void onActivate()
  {
  }
}
