package com.example.misplaced_annotation;

import arez.annotations.ComponentTypeNameRef;

final class ComponentTypeNameRefOutsideArezTypeModel
{
  @ComponentTypeNameRef
  String getComponentTypeName()
  {
    return "";
  }
}
