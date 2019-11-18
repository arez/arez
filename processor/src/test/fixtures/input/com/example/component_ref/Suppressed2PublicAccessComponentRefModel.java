package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.annotations.SuppressArezWarnings;

@ArezComponent( allowEmpty = true )
abstract class Suppressed2PublicAccessComponentRefModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:PublicRefMethod" )
  @ComponentRef
  public abstract Component getComponent();
}
