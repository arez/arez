package com.example.component_state_ref.other;

import arez.annotations.ComponentStateRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseProtectedAccessComponentStateRefModel
{
  @ComponentStateRef
  protected abstract boolean isReady();
}
