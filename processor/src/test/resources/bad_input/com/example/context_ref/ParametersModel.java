package com.example.context_ref;

import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class ParametersModel
{
  @ContextRef
  ArezContext getContext( int i )
  {
    throw new IllegalStateException();
  }
}
