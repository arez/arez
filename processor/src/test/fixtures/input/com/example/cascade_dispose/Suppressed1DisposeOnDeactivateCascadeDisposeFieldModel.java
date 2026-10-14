package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class Suppressed1DisposeOnDeactivateCascadeDisposeFieldModel
{
  @SuppressWarnings( "Arez:ConflictingDisposeModel" )
  @CascadeDispose
  @Nullable MyComponent _myElement;

  @NullMarked
  @ArezComponent( allowEmpty = true, disposeOnDeactivate = true )
  abstract static class MyComponent
  {
  }
}
