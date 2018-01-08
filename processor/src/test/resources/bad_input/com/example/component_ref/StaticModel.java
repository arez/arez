package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class StaticModel
{
  @ComponentRef
  static Component getComponent()
  {
    throw new IllegalStateException();
  }
}
