package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class Suppressed1PublicAccessPreDisposeModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicLifecycleMethod" )
  @PreDispose
  public void preDispose()
  {
  }
}
