package com.example.misplaced_annotation;

import arez.annotations.ContextRef;

final class ContextRefOutsideArezTypeModel
{
  @ContextRef
  Object getContext()
  {
    return null;
  }
}
