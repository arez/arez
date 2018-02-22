package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true, requireEquals = Feature.ENABLE)
public abstract class IntComponentIdRequireEquals
{
  @ComponentId
  public final int getId()
  {
    return 0;
  }
}
