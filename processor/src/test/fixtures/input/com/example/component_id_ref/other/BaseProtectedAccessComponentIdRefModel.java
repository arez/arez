package com.example.component_id_ref.other;

import arez.annotations.ComponentIdRef;

public abstract class BaseProtectedAccessComponentIdRefModel
{
  @ComponentIdRef
  protected abstract int getId();
}
