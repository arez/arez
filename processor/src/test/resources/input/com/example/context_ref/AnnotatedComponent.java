package com.example.context_ref;

import javax.annotation.Nonnull;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ContextRef;

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
