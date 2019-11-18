package com.example.component_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;

@ArezComponent( allowEmpty = true )
abstract class Suppressed1PublicAccessComponentNameRefModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicRefMethod" )
  @ComponentNameRef
  public abstract String getComponentName();
}
