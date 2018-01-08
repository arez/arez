package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;

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
