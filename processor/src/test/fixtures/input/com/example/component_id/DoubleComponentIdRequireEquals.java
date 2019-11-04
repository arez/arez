package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true, requireEquals = Feature.ENABLE )
public abstract class DoubleComponentIdRequireEquals
{
  @ComponentId
  public final double getId()
  {
    return 0;
  }
}
