package com.example.misplaced_annotation;

import arez.annotations.ComponentStateRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class ComponentStateRefOutsideArezTypeModel
{
  @ComponentStateRef
  boolean isReady()
  {
    return true;
  }
}
