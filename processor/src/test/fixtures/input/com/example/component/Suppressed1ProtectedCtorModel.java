package com.example.component;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class Suppressed1ProtectedCtorModel
{
  @SuppressWarnings( "Arez:ProtectedConstructor" )
  protected Suppressed1ProtectedCtorModel()
  {
  }
}
