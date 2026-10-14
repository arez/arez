package com.example.component;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class UnresolvedComponent
{
  static @Nullable Arez_UnresolvedComponent create()
  {
    return new Arez_UnresolvedComponent();
  }
}
