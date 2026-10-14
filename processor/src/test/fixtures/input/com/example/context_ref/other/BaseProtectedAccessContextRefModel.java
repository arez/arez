package com.example.context_ref.other;

import arez.ArezContext;
import arez.annotations.ContextRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseProtectedAccessContextRefModel
{
  @ContextRef
  protected abstract @Nullable ArezContext getContext();
}
