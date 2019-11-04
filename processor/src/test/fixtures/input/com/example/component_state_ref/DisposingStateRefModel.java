package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;
import arez.annotations.State;

@ArezComponent( allowEmpty = true )
public abstract class DisposingStateRefModel
{
  @ComponentStateRef( State.DISPOSING )
  protected abstract boolean isDisposing();
}
