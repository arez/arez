package com.example.component_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
abstract class VoidModel
{
  @ComponentRef
  abstract void getComponent();
}
