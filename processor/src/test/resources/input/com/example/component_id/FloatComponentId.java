package com.example.component_id;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public class FloatComponentId
{
  @ComponentId
  public final float getId()
  {
    return 0;
  }
}
