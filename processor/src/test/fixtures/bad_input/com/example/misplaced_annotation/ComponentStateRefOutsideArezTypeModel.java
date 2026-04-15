package com.example.misplaced_annotation;

import arez.annotations.ComponentStateRef;

final class ComponentStateRefOutsideArezTypeModel
{
  @ComponentStateRef
  boolean isReady()
  {
    return true;
  }
}
