package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;

@ArezComponent( allowEmpty = true )
abstract class Suppressed1ProtectedAccessComponentStateRefModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedRefMethod" )
  @ComponentStateRef
  protected abstract boolean isReady();
}
