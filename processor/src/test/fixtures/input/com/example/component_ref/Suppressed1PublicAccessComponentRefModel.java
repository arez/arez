package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
abstract class Suppressed1PublicAccessComponentRefModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicRefMethod" )
  @ComponentRef
  public abstract Component getComponent();
}
