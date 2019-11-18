package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;
import arez.annotations.State;

@ArezComponent( allowEmpty = true )
abstract class ReadyComponentStateRefModel
{
  @ComponentStateRef( State.READY )
  abstract boolean isReady();
}
