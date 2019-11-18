package com.example.component_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import arez.annotations.SuppressArezWarnings;

@ArezComponent( allowEmpty = true )
abstract class Suppressed2ProtectedAccessComponentNameRefModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedRefMethod" )
  @ComponentNameRef
  protected abstract String getComponentName();
}
