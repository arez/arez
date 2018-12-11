package com.example.component_type_name;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentTypeNameRef;
import java.io.IOException;

@ArezComponent
public abstract class ComponentTypeNameThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ComponentTypeNameRef
  abstract String getTypeName()
    throws IOException;
}
