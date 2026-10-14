package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ObservableZeroOrOneReferenceModel
{
  @Inverse
  @Nullable
  abstract Element getElement();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    abstract @Nullable ObservableZeroOrOneReferenceModel getObservableZeroOrOneReferenceModel();

    @Observable
    @ReferenceId
    abstract int getObservableZeroOrOneReferenceModelId();

    abstract void setObservableZeroOrOneReferenceModelId( int id );
  }
}
