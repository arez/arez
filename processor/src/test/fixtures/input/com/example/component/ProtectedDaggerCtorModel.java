package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( allowEmpty = true, dagger = Feature.ENABLE )
public abstract class ProtectedDaggerCtorModel
{
  @SuppressWarnings( "WeakerAccess" )
  protected ProtectedDaggerCtorModel()
  {
  }
}
