package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;

@ArezComponent( allowEmpty = true )
abstract class NonStandardNameComponentId
{
  @ComponentId
  public int $$id$$()
  {
    return 0;
  }
}
