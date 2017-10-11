package com.example.component_id;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public class DoubleComponentId
{
  @ComponentId
  public final double getId()
  {
    return 0;
  }
}
