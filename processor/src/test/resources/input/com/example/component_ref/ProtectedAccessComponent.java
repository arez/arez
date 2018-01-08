package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class ProtectedAccessComponent
{
  @ComponentRef
  protected Component getComponent()
  {
    throw new IllegalStateException();
  }
}
