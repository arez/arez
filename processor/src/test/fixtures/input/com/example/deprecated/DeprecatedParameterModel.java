package com.example.deprecated;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class DeprecatedParameterModel
{
  @SuppressWarnings( "deprecation" )
  @Action
  void doAction( @Nullable MyDeprecatedEntity entity )
  {
  }
}
