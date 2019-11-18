package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;
import arez.annotations.State;

@ArezComponent( allowEmpty = true )
abstract class MultipleComponentStateRefModel
{
  @ComponentStateRef( State.CONSTRUCTED )
  abstract boolean isConstructed1();

  @ComponentStateRef( State.COMPLETE )
  abstract boolean isComplete1();

  @ComponentStateRef( State.READY )
  abstract boolean isReady1();

  @ComponentStateRef( State.DISPOSING )
  abstract boolean isDisposing1();

  @ComponentStateRef( State.CONSTRUCTED )
  abstract boolean isConstructed2();

  @ComponentStateRef( State.COMPLETE )
  abstract boolean isComplete2();

  @ComponentStateRef( State.READY )
  abstract boolean isReady2();

  @ComponentStateRef( State.DISPOSING )
  abstract boolean isDisposing2();
}
