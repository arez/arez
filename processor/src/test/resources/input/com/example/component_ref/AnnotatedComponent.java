package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import javax.annotation.Nonnull;

@ArezComponent( allowEmpty = true )
class AnnotatedComponent
{
  @Nonnull
  @ComponentRef
  public Component getComponent()
  {
    throw new IllegalStateException();
  }
}
