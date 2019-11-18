package com.example.component_state_ref.other;

import arez.annotations.ComponentStateRef;

public abstract class BaseProtectedAccessComponentStateRefModel
{
  @ComponentStateRef
  protected abstract boolean isReady();
}
