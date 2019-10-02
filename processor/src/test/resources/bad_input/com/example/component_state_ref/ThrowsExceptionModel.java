package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;

@ArezComponent( allowEmpty = true )
abstract class ThrowsExceptionModel
{
  @ComponentStateRef
  abstract boolean isReady()
    throws Exception;
}
