package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;

@ArezComponent
abstract class Suppressed1DisposeOnDeactivateCascadeDisposeFieldModel
{
  @SuppressWarnings( "Arez:ConflictingDisposeModel" )
  @CascadeDispose
  MyComponent _myElement;

  @ArezComponent( allowEmpty = true, disposeOnDeactivate = true )
  abstract static class MyComponent
  {
  }
}
