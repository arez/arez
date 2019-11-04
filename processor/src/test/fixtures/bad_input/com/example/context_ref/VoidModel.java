package com.example.context_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
abstract class VoidModel
{
  @ContextRef
  abstract void getContext();
}
