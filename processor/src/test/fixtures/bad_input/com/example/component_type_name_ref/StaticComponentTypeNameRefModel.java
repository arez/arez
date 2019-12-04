package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent( allowEmpty = true )
public abstract class StaticComponentTypeNameRefModel
{
  @ComponentTypeNameRef
  static String getTypeName()
  {
    return null;
  }
}
