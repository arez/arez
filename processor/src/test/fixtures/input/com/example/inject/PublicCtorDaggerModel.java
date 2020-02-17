package com.example.inject;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( service = Feature.ENABLE, allowEmpty = true, sting = Feature.DISABLE )
public abstract class PublicCtorDaggerModel
{
  public PublicCtorDaggerModel()
  {
  }
}
