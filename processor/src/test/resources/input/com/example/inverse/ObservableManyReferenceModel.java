package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.List;
import javax.annotation.Nonnull;

@ArezComponent
abstract class ObservableManyReferenceModel
{
  @Inverse
  @Nonnull
  abstract List<Element> getElements();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract ObservableManyReferenceModel getObservableManyReferenceModel();

    @Observable
    @ReferenceId
    abstract int getObservableManyReferenceModelId();

    abstract void setObservableManyReferenceModelId( int id );
  }
}
