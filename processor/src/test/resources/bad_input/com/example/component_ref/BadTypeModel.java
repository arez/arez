package com.example.component_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class BadTypeModel
{
  @ComponentRef
  String getComponent()
  {
    throw new IllegalStateException();
  }
}
