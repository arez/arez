package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
abstract class FloatComponentId
{
  @ComponentId
  public float getId()
  {
    return 0;
  }
}
