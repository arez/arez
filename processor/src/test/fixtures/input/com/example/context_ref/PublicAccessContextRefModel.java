package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
abstract class PublicAccessContextRefModel
{
  @ContextRef
  public abstract ArezContext getContext();
}
