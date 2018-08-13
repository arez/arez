package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class DefaultMultiplicityInverseModel
{
  @Inverse
  abstract Collection<Element> getElements();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract DefaultMultiplicityInverseModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
