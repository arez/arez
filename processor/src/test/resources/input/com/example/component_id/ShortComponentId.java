package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public abstract class ShortComponentId
{
  @ComponentId
  public final short getId()
  {
    return 0;
  }
}
