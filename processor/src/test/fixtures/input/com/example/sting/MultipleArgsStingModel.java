package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( sting = Feature.ENABLE, allowEmpty = true  )
public abstract class MultipleArgsStingModel
{
  MultipleArgsStingModel( int i, String foo )
  {
  }
}
