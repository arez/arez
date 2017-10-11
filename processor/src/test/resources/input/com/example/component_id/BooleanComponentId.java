package com.example.component_id;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public class BooleanComponentId
{
  @ComponentId
  public final boolean getId()
  {
    return false;
  }
}
