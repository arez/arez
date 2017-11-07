package com.example.component_ref;

import org.realityforge.arez.Component;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentRef;

@ArezComponent( allowEmpty = true )
class ProtectedAccessComponent
{
  @ComponentRef
  protected Component getComponent()
  {
    throw new IllegalStateException();
  }
}
