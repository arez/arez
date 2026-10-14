package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( sting = Feature.ENABLE, allowEmpty = true  )
public abstract class MultipleArgsStingModel
{
  MultipleArgsStingModel( int i, @Nullable String foo )
  {
  }
}
