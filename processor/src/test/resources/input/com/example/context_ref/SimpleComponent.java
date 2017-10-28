package com.example.context_ref;

import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class SimpleComponent
{
  @ContextRef
  ArezContext getContext()
  {
    throw new IllegalStateException();
  }
}
