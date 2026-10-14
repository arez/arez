package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class DefaultMultiplicityInverseModel
{
  @Inverse
  abstract @Nullable Collection<Element> getElements();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract @Nullable DefaultMultiplicityInverseModel getDefaultMultiplicityInverseModel();

    @ReferenceId
    int getDefaultMultiplicityInverseModelId()
    {
      return 0;
    }
  }
}
