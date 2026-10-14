package com.example.component_name_ref.other;

import arez.annotations.ComponentNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseProtectedAccessComponentNameRefModel
{
  @ComponentNameRef
  protected abstract @Nullable String getComponentName();
}
