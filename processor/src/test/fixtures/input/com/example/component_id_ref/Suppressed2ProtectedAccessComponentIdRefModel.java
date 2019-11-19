package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentIdRef;
import arez.annotations.SuppressArezWarnings;

@ArezComponent( allowEmpty = true )
abstract class Suppressed2ProtectedAccessComponentIdRefModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedRefMethod" )
  @ComponentIdRef
  protected abstract int getId();
}
