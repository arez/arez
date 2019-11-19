package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;

@ArezComponent( allowEmpty = true )
abstract class PublicAccessViaInterfaceContextRefModel
  implements ContextRefInterface
{
  @Override
  @ContextRef
  public abstract ArezContext getContext();
}
