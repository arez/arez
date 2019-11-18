package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent( allowEmpty = true )
abstract class NonStandardMethodNameComponentTypeNameRefModel
{
  @ComponentTypeNameRef
  abstract String $$$getTypeName$$$();
}
