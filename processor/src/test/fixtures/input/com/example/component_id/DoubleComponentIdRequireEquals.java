package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true, requireEquals = Feature.ENABLE )
abstract class DoubleComponentIdRequireEquals
{
  @ComponentId
  public double getId()
  {
    return 0;
  }
}
