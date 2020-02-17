package com.example.dagger;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( service = Feature.ENABLE, sting = Feature.DISABLE, allowEmpty = true )
public abstract class PublicCtorDaggerModel
{
  public PublicCtorDaggerModel()
  {
  }
}
