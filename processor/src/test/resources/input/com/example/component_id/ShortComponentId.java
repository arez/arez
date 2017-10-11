package com.example.component_id;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public class ShortComponentId
{
  @ComponentId
  public final short getId()
  {
    return 0;
  }
}
