package com.example.component_type_name_ref.other;

import arez.annotations.ComponentTypeNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseProtectedAccessComponentTypeNameRefModel
{
  @ComponentTypeNameRef
  protected abstract @Nullable String getTypeName();
}
