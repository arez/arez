package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class DisableInverseModel
{
  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.DISABLE )
    abstract @Nullable DisableInverseModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
