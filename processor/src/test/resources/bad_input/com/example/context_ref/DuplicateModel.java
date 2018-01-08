package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
class DuplicateModel
{
  @ContextRef
  ArezContext getContext()
  {
    throw new IllegalStateException();
  }

  @ContextRef
  ArezContext getContext2()
  {
    throw new IllegalStateException();
  }
}
