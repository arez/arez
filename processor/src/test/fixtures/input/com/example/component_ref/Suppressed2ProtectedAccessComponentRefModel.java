package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.annotations.SuppressArezWarnings;

@ArezComponent( allowEmpty = true )
abstract class Suppressed2ProtectedAccessComponentRefModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedRefMethod" )
  @ComponentRef
  protected abstract Component getComponent();
}
