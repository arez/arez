package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PreDispose;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class Suppressed2PublicAccessPreDisposeModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:PublicLifecycleMethod" )
  @PreDispose
  public void preDispose()
  {
  }
}
