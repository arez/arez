package com.example.misplaced_annotation;

import arez.annotations.Inverse;

final class InverseOutsideArezTypeModel
{
  @Inverse
  Object getInverse()
  {
    return null;
  }
}
