package com.example.deprecated;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class DeprecatedParameterModel
{
  @SuppressWarnings( "deprecation" )
  @Action
  void doAction( MyDeprecatedEntity entity )
  {
  }
}
