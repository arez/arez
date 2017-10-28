package com.example.context_ref;

import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class StaticModel
{
  @ContextRef
  static ArezContext getContext()
  {
    throw new IllegalStateException();
  }
}
