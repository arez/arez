package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
public abstract class NonStandardNameComponentId
{
  @ComponentId
  public final int $$id$$()
  {
    return 0;
  }
}
