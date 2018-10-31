package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Computed;

@ArezComponent
public abstract class BadDepTypeMemoizeModel
{
  @Computed( depType = DepType.AREZ_OR_EXTERNAL )
  int getField( int key )
  {
    return 0;
  }
}
