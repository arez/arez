package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class StaticComponentTypeNameRefModel
{
  @ComponentTypeNameRef
  static @Nullable String getTypeName()
  {
    return null;
  }
}
