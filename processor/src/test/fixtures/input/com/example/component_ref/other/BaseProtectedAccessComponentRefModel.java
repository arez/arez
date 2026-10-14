package com.example.component_ref.other;

import arez.Component;
import arez.annotations.ComponentRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseProtectedAccessComponentRefModel
{
  @ComponentRef
  protected abstract @Nullable Component getComponent();
}
