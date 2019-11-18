package com.example.component_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import arez.annotations.SuppressArezWarnings;

@ArezComponent( allowEmpty = true )
abstract class Suppressed2PublicAccessComponentNameRefModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:PublicRefMethod" )
  @ComponentNameRef
  public abstract String getComponentName();
}
