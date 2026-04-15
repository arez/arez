package com.example.misplaced_annotation;

import arez.annotations.ComputableValueRef;

final class ComputableValueRefOutsideArezTypeModel
{
  @ComputableValueRef
  Object getNameComputableValue()
  {
    return null;
  }
}
