package com.example.component_ref;

import org.realityforge.arez.Component;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentRef;

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
