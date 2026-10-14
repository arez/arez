package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class Suppressed1ProtectedAccessComponentStateRefModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedMethod" )
  @ComponentStateRef
  protected abstract boolean isReady();
}
