package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;
import arez.annotations.SuppressArezWarnings;

@ArezComponent( allowEmpty = true )
abstract class Suppressed2PublicAccessComponentTypeNameRefModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:PublicRefMethod" )
  @ComponentTypeNameRef
  public abstract String getTypeName();
}
