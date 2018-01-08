package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class ProtectedAccessComponent
{
  @ContextRef
  protected ArezContext getContext()
  {
    throw new IllegalStateException();
  }
}
