package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import javax.annotation.Nonnull;

@ArezComponent( allowEmpty = true )
class AnnotatedComponent
{
  @Nonnull
  @ContextRef
  public ArezContext getContext()
  {
    throw new IllegalStateException();
  }
}
