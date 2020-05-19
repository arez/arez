package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true, requireEquals = Feature.ENABLE )
abstract class ObjectComponentIdRequireEquals
{
  @ComponentId
  public String getId()
  {
    return "";
  }
}
