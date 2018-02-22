package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true, requireEquals = Feature.ENABLE )
public abstract class LongComponentIdRequireEquals
{
  @ComponentId
  public final long getId()
  {
    return 0;
  }
}
