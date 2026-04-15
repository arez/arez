package com.example.misplaced_annotation;

import arez.annotations.ComponentNameRef;

final class ComponentNameRefOutsideArezTypeModel
{
  @ComponentNameRef
  String getComponentName()
  {
    return "";
  }
}
