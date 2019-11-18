package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent( allowEmpty = true )
abstract class BasicComponentTypeNameRefModel
{
  @ComponentTypeNameRef
  abstract String getTypeName();
}
