package com.example.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class MultiComponentRefModel
{
  @ComponentRef
  abstract @Nullable Component getComponent();

  @ComponentRef
  abstract @Nullable Component getComponent2();
}
