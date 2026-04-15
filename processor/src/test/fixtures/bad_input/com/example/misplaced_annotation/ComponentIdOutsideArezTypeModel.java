package com.example.misplaced_annotation;

import arez.annotations.ComponentId;

final class ComponentIdOutsideArezTypeModel
{
  @ComponentId
  int getId()
  {
    return 1;
  }
}
