package com.example.post_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.PostDispose;

@ArezComponent( allowEmpty = true )
abstract class Suppressed1ProtectedAccessPostDisposeModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedMethod" )
  @PostDispose
  protected void postDispose()
  {
  }
}
