package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.ComponentIdRef;

@ArezComponent( allowEmpty = true )
public abstract class BadType2Model
{
  @ComponentId
  final short getComponentId()
  {
    return 42;
  }

  @ComponentIdRef
  abstract String getId();
}
