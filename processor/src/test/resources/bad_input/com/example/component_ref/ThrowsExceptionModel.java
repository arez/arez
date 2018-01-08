package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class ThrowsExceptionModel
{
  @ComponentRef
  Component getComponent()
    throws Exception
  {
    throw new IllegalStateException();
  }
}
