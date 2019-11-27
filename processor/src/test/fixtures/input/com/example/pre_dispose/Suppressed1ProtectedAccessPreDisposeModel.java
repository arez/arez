package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;

@ArezComponent( allowEmpty = true )
abstract class Suppressed1ProtectedAccessPreDisposeModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedLifecycleMethod" )
  @PreDispose
  protected void preDispose()
  {
  }
}
