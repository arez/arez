package com.example.misplaced_annotation;

import arez.annotations.Action;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class ActionOutsideArezTypeModel
{
  @Action
  void perform()
  {
  }
}
