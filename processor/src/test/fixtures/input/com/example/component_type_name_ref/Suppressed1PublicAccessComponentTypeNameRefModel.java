package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;

@ArezComponent( allowEmpty = true )
abstract class Suppressed1PublicAccessComponentTypeNameRefModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicRefMethod" )
  @ComponentTypeNameRef
  public abstract String getTypeName();
}
