package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableReferenceInverseModel
{
  @Inverse
  abstract @Nullable Collection<Element> getElements();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract @Nullable ObservableReferenceInverseModel getObservableReferenceInverseModel();

    @Observable
    @ReferenceId
    abstract int getObservableReferenceInverseModelId();

    abstract void setObservableReferenceInverseModelId( int id );
  }
}
