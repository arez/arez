package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.SuppressArezWarnings;

@ArezComponent( allowEmpty = true )
abstract class Suppressed2ProtectedCtorModel
{
  @SuppressArezWarnings( "Arez:ProtectedConstructor" )
  protected Suppressed2ProtectedCtorModel()
  {
  }
}
