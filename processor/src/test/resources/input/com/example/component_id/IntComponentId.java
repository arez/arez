package com.example.component_id;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public class IntComponentId
{
  @ComponentId
  public final int getId()
  {
    return 0;
  }
}
