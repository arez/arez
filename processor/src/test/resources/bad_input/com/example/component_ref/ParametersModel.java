package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class ParametersModel
{
  @ComponentRef
  Component getComponent(int i)
  {
    throw new IllegalStateException();
  }
}
