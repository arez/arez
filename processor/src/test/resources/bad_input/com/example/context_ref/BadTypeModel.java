package com.example.context_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class BadTypeModel
{
  @ContextRef
  String getContext()
  {
    throw new IllegalStateException();
  }
}
