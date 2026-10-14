package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class Suppressed1ProtectedAccessFieldCascadeDisposeModel
{
  @SuppressWarnings( "Arez:ProtectedField" )
  @CascadeDispose
  protected @Nullable Disposable _myElement;
}
