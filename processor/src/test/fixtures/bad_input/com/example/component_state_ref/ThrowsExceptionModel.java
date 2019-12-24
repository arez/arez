package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;

@ArezComponent( allowEmpty = true )
abstract class ThrowsExceptionModel
{
  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ComponentStateRef
  abstract boolean isReady()
    throws Exception;
}
