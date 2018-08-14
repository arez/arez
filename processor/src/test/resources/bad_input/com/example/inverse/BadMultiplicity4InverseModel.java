package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;

@ArezComponent
abstract class BadMultiplicity4InverseModel
{
  @Inverse
  @Nonnull
  abstract MyEntity getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract BadMultiplicity4InverseModel getBadMultiplicity4InverseModel();

    @ReferenceId
    int getBadMultiplicity4InverseModelId()
    {
      return 0;
    }
  }
}
