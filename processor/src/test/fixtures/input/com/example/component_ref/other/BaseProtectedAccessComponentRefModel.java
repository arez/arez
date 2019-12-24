package com.example.component_ref.other;

import arez.Component;
import arez.annotations.ComponentRef;

public abstract class BaseProtectedAccessComponentRefModel
{
  @ComponentRef
  protected abstract Component getComponent();
}
