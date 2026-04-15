package com.example.misplaced_annotation;

import arez.annotations.Action;

abstract class ActionOutsideArezTypeModel
{
  @Action
  void perform()
  {
  }
}
