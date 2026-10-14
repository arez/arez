package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableSetInverseModel
{
  @Observable
  @Inverse
  abstract @Nullable Set<Element> getElements();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract @Nullable ObservableSetInverseModel getObservableSetInverseModel();

    @ReferenceId
    int getObservableSetInverseModelId()
    {
      return 0;
    }
  }
}
