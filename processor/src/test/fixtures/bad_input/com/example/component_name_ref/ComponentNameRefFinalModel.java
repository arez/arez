package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ComponentNameRefFinalModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  final @Nullable String getTypeName()
  {
    return null;
  }
}
