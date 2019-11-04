package com.example.component_state_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentStateRef;
import arez.annotations.State;

@ArezComponent( allowEmpty = true )
public abstract class MultipleStateRefModel
{
  @ComponentStateRef( State.CONSTRUCTED )
  protected abstract boolean isConstructed1();

  @ComponentStateRef( State.COMPLETE )
  protected abstract boolean isComplete1();

  @ComponentStateRef( State.READY )
  protected abstract boolean isReady1();

  @ComponentStateRef( State.DISPOSING )
  protected abstract boolean isDisposing1();

  @ComponentStateRef( State.CONSTRUCTED )
  protected abstract boolean isConstructed2();

  @ComponentStateRef( State.COMPLETE )
  protected abstract boolean isComplete2();

  @ComponentStateRef( State.READY )
  protected abstract boolean isReady2();

  @ComponentStateRef( State.DISPOSING )
  protected abstract boolean isDisposing2();
}
