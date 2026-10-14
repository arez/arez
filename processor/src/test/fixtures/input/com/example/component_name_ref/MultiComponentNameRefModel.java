package com.example.component_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class MultiComponentNameRefModel
{
  @ComponentNameRef
  abstract @Nullable String getTypeName();

  @ComponentNameRef
  abstract @Nullable String getTypeName2();
}
