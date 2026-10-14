package com.example.component;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ProtectedCtorModel
{
  @SuppressWarnings( "WeakerAccess" )
  protected ProtectedCtorModel()
  {
  }
}
