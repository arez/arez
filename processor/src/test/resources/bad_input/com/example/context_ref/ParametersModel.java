package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class ParametersModel
{
  @ContextRef
  ArezContext getContext( int i )
  {
    throw new IllegalStateException();
  }
}
