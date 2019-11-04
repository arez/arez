package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import javax.annotation.Nonnull;

@ArezComponent( allowEmpty = true )
abstract class AnnotatedComponent
{
  @Nonnull
  @ContextRef
  public abstract ArezContext getContext();
}
