package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent( allowEmpty = true )
abstract class DisableInverseModel
{
  @ArezComponent
  static abstract class Element
  {
    @Reference( inverse = Feature.DISABLE )
    abstract DisableInverseModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
