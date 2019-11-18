package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
abstract class Suppressed1ProtectedAccessComponentRefModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedRefMethod" )
  @ComponentRef
  protected abstract Component getComponent();
}
