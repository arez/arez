package com.example.component_name_ref;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import java.io.IOException;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ComponentNameRefThrowsExceptionModel
{
  @Action
  void myAction()
  {
  }

  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @ComponentNameRef
  abstract @Nullable String getTypeName()
    throws IOException;
}
