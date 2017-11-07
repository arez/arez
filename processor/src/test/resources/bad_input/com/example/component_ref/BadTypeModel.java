package com.example.component_ref;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class BadTypeModel
{
  @ComponentRef
  String getComponent()
  {
    throw new IllegalStateException();
  }
}
