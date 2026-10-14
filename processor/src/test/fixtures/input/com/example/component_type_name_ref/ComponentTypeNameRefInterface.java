package com.example.component_type_name_ref;

import arez.annotations.ComponentTypeNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ComponentTypeNameRefInterface
{
  @ComponentTypeNameRef
  @Nullable String getTypeName();
}
