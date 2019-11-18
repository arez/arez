package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;
import arez.annotations.State;

@ArezComponent( allowEmpty = true )
abstract class CompleteComponentStateRefModel
{
  @ComponentStateRef( State.COMPLETE )
  abstract boolean isComplete();
}
