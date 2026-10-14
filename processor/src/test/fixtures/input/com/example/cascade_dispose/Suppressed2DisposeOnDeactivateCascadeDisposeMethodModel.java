package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class Suppressed2DisposeOnDeactivateCascadeDisposeMethodModel
{
  @SuppressArezWarnings( "Arez:ConflictingDisposeModel" )
  @CascadeDispose
  @Nullable MyComponent myElement()
  {
    return null;
  }

  @NullMarked
  @ArezComponent( allowEmpty = true, disposeOnDeactivate = true )
  abstract static class MyComponent
  {
  }
}
