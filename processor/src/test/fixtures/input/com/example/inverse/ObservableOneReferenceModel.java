package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;

@ArezComponent
abstract class ObservableOneReferenceModel
{
  @Inverse
  @Nonnull
  abstract Element getElement();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract ObservableOneReferenceModel getObservableOneReferenceModel();

    @Observable
    @ReferenceId
    abstract int getObservableOneReferenceModelId();

    abstract void setObservableOneReferenceModelId( int id );
  }
}
