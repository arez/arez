package com.example.context_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class VoidModel
{
  @ContextRef
  abstract void getContext();
}
