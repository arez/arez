package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
abstract class ThrowsExceptionModel
{
  @ComponentRef
  abstract Component getComponent()
    throws Exception;
}
