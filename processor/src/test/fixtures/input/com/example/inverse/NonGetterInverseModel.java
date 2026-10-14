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
abstract class NonGetterInverseModel
{
  @Observable
  @Inverse
  abstract @Nullable Set<Element> elements();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract @Nullable NonGetterInverseModel getNonGetterInverseModel();

    @ReferenceId
    int getNonGetterInverseModelId()
    {
      return 0;
    }
  }
}
