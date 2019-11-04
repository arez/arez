package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public abstract class DoubleComponentId
{
  @ComponentId
  public final double getId()
  {
    return 0;
  }
}
