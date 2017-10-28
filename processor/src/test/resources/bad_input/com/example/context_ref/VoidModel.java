package com.example.context_ref;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class VoidModel
{
  @ContextRef
  void getContext()
  {
    throw new IllegalStateException();
  }
}
