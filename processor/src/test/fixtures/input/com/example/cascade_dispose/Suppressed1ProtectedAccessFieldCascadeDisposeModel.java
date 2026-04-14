package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
abstract class Suppressed1ProtectedAccessFieldCascadeDisposeModel
{
  @SuppressWarnings( "Arez:ProtectedField" )
  @CascadeDispose
  protected Disposable _myElement;
}
