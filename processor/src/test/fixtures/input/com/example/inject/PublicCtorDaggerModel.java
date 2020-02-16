package com.example.inject;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( service = Feature.ENABLE, allowEmpty = true )
public abstract class PublicCtorDaggerModel
{
  public PublicCtorDaggerModel()
  {
  }
}
