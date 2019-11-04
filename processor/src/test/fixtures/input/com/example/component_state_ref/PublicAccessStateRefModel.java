package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;

@ArezComponent( allowEmpty = true )
public abstract class PublicAccessStateRefModel
{
  @ComponentStateRef
  public abstract boolean isReady();
}
