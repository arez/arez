package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.SuppressArezWarnings;

@ArezComponent
abstract class Suppressed2ProtectedAccessFieldCascadeDisposeModel
{
  @SuppressArezWarnings( "Arez:ProtectedField" )
  @CascadeDispose
  protected Disposable _myElement;
}
