package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ArezComponent
abstract class NonObservableNullableManyReferenceModel
{
  @Inverse
  @Nonnull
  abstract List<Element> getElements();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    @Nullable
    abstract NonObservableNullableManyReferenceModel getNonObservableNullableManyReferenceModel();

    @Nullable
    @ReferenceId
    Integer getNonObservableNullableManyReferenceModelId()
    {
      return null;
    }
  }
}
