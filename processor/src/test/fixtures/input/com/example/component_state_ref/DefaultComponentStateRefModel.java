package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;

@ArezComponent( allowEmpty = true )
abstract class DefaultComponentStateRefModel
{
  @ComponentStateRef
  abstract boolean isReady();
}
