package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public class BooleanComponentId
{
  @ComponentId
  public final boolean getId()
  {
    return false;
  }
}
