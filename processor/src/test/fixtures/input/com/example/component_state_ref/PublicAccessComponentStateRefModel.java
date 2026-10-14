package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class PublicAccessComponentStateRefModel
{
  @ComponentStateRef
  public abstract boolean isReady();
}
