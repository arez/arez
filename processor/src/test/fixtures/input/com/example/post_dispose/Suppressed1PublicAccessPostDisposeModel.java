package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
