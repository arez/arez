package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableOneReferenceModel
{
  @Inverse
  @NonNull
  abstract Element getElement();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract @Nullable ObservableOneReferenceModel getObservableOneReferenceModel();

    @Observable
    @ReferenceId
    abstract int getObservableOneReferenceModelId();

    abstract void setObservableOneReferenceModelId( int id );
  }
}
