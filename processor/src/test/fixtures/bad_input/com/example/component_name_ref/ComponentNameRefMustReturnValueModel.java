package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ComponentNameRefMustReturnValueModel
{
  @Action
  void myAction()
  {
  }

  @ComponentNameRef
  abstract void getTypeName();
}
