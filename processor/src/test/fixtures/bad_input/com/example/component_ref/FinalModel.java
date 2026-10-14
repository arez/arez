package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class FinalModel
{
  @ComponentRef
  final @Nullable Component getComponent()
  {
    throw new IllegalStateException();
  }
}
