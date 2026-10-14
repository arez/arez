package com.example.component_name_ref;

import arez.annotations.ComponentNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ComponentNameRefInterface
{
  @ComponentNameRef
  @Nullable String getComponentName();
}
