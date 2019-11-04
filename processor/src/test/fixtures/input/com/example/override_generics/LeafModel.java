package com.example.override_generics;

import arez.annotations.ArezComponent;
import javax.annotation.Nonnull;

@ArezComponent( allowEmpty = true )
public abstract class LeafModel
  extends MiddleModel
{
  @Override
  protected final void myAbstractMethod( @Nonnull final String value )
  {
  }
}
