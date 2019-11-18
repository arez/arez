package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent( nameIncludesId = false, allowEmpty = true )
abstract class SingletonComponentTypeNameRefModel
{
  @ComponentTypeNameRef
  abstract String getTypeName();
}
