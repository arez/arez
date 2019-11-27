package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent( allowEmpty = true )
abstract class Suppressed1PublicAccessPostDisposeModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicLifecycleMethod" )
  @PostDispose
  public void postDispose()
  {
  }
}
