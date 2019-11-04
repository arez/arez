package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true, requireEquals = Feature.ENABLE )
public abstract class CharComponentIdRequireEquals
{
  @ComponentId
  public final char getId()
  {
    return 'A';
  }
}
