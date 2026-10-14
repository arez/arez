package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NonObservableNullableManyReferenceModel
{
  @Inverse
  @NonNull
  abstract List<Element> getElements();

  @NullMarked
  @ArezComponent
  abstract static class Element
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
