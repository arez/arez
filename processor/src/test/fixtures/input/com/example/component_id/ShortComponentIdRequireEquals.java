package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true, requireEquals = Feature.ENABLE )
abstract class ShortComponentIdRequireEquals
{
  @ComponentId
  public short getId()
  {
    return 0;
  }
}
