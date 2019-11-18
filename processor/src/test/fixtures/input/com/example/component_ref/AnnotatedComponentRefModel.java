package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import javax.annotation.Nonnull;

@ArezComponent( allowEmpty = true )
abstract class AnnotatedComponentRefModel
{
  @Nonnull
  @ComponentRef
  abstract Component getComponent();
}
