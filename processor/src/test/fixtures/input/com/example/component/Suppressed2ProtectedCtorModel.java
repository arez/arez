package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class Suppressed2ProtectedCtorModel
{
  @SuppressArezWarnings( "Arez:ProtectedConstructor" )
  protected Suppressed2ProtectedCtorModel()
  {
  }
}
