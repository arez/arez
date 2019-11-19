package com.example.context_ref.other;

import arez.ArezContext;
import arez.annotations.ContextRef;

public abstract class BaseProtectedAccessContextRefModel
{
  @ContextRef
  protected abstract ArezContext getContext();
}
