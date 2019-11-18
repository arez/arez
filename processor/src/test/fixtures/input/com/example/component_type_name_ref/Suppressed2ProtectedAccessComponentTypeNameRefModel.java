package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;
import arez.annotations.SuppressArezWarnings;

@ArezComponent( allowEmpty = true )
abstract class Suppressed2ProtectedAccessComponentTypeNameRefModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedRefMethod" )
  @ComponentTypeNameRef
  protected abstract String getTypeName();
}
