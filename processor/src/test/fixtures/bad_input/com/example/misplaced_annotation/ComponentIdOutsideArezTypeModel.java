package com.example.misplaced_annotation;

import arez.annotations.ComponentId;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class ComponentIdOutsideArezTypeModel
{
  @ComponentId
  int getId()
  {
    return 1;
  }
}
