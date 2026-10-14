package com.example.package_access.other;

import arez.ArezContext;
import arez.annotations.ContextRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseContextRefModel
{
  @ContextRef
  abstract @Nullable ArezContext getContext();
}
