package com.example.misplaced_annotation;

import arez.annotations.ComponentRef;

final class ComponentRefOutsideArezTypeModel
{
  @ComponentRef
  Object getComponent()
  {
    return null;
  }
}
