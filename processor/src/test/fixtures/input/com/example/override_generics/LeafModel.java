package com.example.override_generics;

import arez.annotations.ArezComponent;
import javax.annotation.Nonnull;

@ArezComponent( allowEmpty = true )
abstract class LeafModel
  extends MiddleModel
{
  @Override
  protected void myAbstractMethod( @Nonnull final String value )
  {
  }
}
