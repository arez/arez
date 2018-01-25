package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public abstract class ByteComponentId
{
  @ComponentId
  public final byte getId()
  {
    return 0;
  }
}
