package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public abstract class IntComponentId
{
  @ComponentId
  public final int getId()
  {
    return 0;
  }
}
