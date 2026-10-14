package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class Suppressed2ProtectedAccessFieldCascadeDisposeModel
{
  @SuppressArezWarnings( "Arez:ProtectedField" )
  @CascadeDispose
  protected @Nullable Disposable _myElement;
}
