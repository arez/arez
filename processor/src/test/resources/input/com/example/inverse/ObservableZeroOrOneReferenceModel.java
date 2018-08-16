package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nullable;

@ArezComponent
abstract class ObservableZeroOrOneReferenceModel
{
  @Inverse
  @Nullable
  abstract Element getElement();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    abstract ObservableZeroOrOneReferenceModel getObservableZeroOrOneReferenceModel();

    @Observable
    @ReferenceId
    abstract int getObservableZeroOrOneReferenceModelId();

    abstract void setObservableZeroOrOneReferenceModelId( int id );
  }
}
