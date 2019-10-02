package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;
import arez.annotations.State;

@ArezComponent( allowEmpty = true )
public abstract class ConstructedStateRefModel
{
  @ComponentStateRef( State.CONSTRUCTED )
  protected abstract boolean isConstructed();
}
