package com.example.context_ref;

import arez.ArezContext;
import arez.annotations.ContextRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
interface ContextRefInterface
{
  @ContextRef
  @Nullable ArezContext getContext();
}
