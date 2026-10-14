package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( sting = Feature.ENABLE, allowEmpty = true )
public abstract class PublicCtorStingModel
{
  public PublicCtorStingModel()
  {
  }
}
