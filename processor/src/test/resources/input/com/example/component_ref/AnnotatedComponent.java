package com.example.component_ref;

import javax.annotation.Nonnull;
import org.realityforge.arez.Component;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentRef;

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
