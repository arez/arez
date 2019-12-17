package com.example.component;

import arez.annotations.ArezComponent;

@ArezComponent( allowEmpty = true )
abstract class ProtectedCtorModel
{
  @SuppressWarnings( "WeakerAccess" )
  protected ProtectedCtorModel()
  {
  }
}
