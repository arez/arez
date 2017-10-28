package com.example.context_ref;

import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class ThrowsExceptionModel
{
  @ContextRef
  ArezContext getContext()
    throws Exception
  {
    throw new IllegalStateException();
  }
}
