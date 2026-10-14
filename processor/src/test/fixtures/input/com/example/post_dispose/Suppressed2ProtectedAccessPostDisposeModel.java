package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class Suppressed2ProtectedAccessPostDisposeModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedMethod" )
  @PostDispose
  protected void postDispose()
  {
  }
}
