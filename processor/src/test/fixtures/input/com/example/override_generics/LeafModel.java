package com.example.override_generics;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class LeafModel
  extends MiddleModel
{
  @Override
  protected void myAbstractMethod( @NonNull final String value )
  {
  }
}
