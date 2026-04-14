package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.SuppressArezWarnings;

@ArezComponent
abstract class Suppressed2DisposeOnDeactivateCascadeDisposeMethodModel
{
  @SuppressArezWarnings( "Arez:ConflictingDisposeModel" )
  @CascadeDispose
  MyComponent myElement()
  {
    return null;
  }

  @ArezComponent( allowEmpty = true, disposeOnDeactivate = true )
  abstract static class MyComponent
  {
  }
}
