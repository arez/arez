package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
abstract class BooleanComponentId
{
  @ComponentId
  public boolean getId()
  {
    return false;
  }
}
