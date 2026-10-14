package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentIdRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class MultiComponentIdRefModel
{
  @ComponentIdRef
  abstract int getId();

  @ComponentIdRef
  abstract int getId2();
}
