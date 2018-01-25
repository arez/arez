package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import javax.annotation.Nonnull;

@ArezComponent( allowEmpty = true )
abstract class AnnotatedComponent
{
  @Nonnull
  @ComponentRef
  public abstract Component getComponent();
}
