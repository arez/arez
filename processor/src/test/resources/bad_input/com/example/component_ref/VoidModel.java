package com.example.component_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class VoidModel
{
  @ComponentRef
  void getComponent()
  {
    throw new IllegalStateException();
  }
}
