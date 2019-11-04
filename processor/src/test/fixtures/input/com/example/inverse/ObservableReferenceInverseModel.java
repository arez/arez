package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class ObservableReferenceInverseModel
{
  @Inverse
  abstract Collection<Element> getElements();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract ObservableReferenceInverseModel getObservableReferenceInverseModel();

    @Observable
    @ReferenceId
    abstract int getObservableReferenceInverseModelId();

    abstract void setObservableReferenceInverseModelId( int id );
  }
}
