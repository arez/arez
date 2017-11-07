package com.example.component_ref;

import org.realityforge.arez.ArezContext;
import org.realityforge.arez.Component;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentRef;
import org.realityforge.arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class StaticModel
{
  @ComponentRef
  static Component getComponent()
  {
    throw new IllegalStateException();
  }
}
