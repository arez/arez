package com.example.context_ref;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class BadTypeModel
{
  @ContextRef
  String getContext()
  {
    throw new IllegalStateException();
  }
}
