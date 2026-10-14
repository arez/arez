package com.example.component_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class BadTypeModel
{
  @ComponentRef
  abstract @Nullable String getComponent();
}
