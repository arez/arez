package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
abstract class ObjectComponentId
{
  @ComponentId
  public String getId()
  {
    return "";
  }
}
