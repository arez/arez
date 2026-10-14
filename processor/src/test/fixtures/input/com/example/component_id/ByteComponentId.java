package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ByteComponentId
{
  @ComponentId
  public byte getId()
  {
    return 0;
  }
}
