package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( sting = Feature.ENABLE, dagger = Feature.DISABLE, allowEmpty = true )
public abstract class PublicCtorStingModel
{
  public PublicCtorStingModel()
  {
  }
}
