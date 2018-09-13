package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentIdRef;

@ArezComponent( allowEmpty = true )
public abstract class NoReturnModel
{
  @ComponentIdRef
  abstract void getId();
}
