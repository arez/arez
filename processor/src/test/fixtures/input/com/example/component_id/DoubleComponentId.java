package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
abstract class DoubleComponentId
{
  @ComponentId
  public double getId()
  {
    return 0;
  }
}
