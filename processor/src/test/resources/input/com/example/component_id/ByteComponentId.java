package com.example.component_id;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public class ByteComponentId
{
  @ComponentId
  public final byte getId()
  {
    return 0;
  }
}
