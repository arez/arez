package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class FinalModel
{
  @ComponentRef
  final Component getComponent()
  {
    throw new IllegalStateException();
  }
}
