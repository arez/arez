package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
abstract class ProtectedAccessContextRefModel
{
  @ContextRef
  protected abstract ArezContext getContext();
}
