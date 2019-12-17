package com.example.component;

import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
abstract class Suppressed1ProtectedCtorModel
{
  @SuppressWarnings( "Arez:ProtectedConstructor" )
  protected Suppressed1ProtectedCtorModel()
  {
  }
}
