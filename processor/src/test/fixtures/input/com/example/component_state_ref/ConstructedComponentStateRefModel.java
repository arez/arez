package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;
import arez.annotations.State;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ConstructedComponentStateRefModel
{
  @ComponentStateRef( State.CONSTRUCTED )
  abstract boolean isConstructed();
}
