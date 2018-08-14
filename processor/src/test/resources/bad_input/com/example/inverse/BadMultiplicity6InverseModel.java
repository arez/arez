package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nullable;

@ArezComponent
abstract class BadMultiplicity6InverseModel
{
  @Inverse
  @Nullable
  abstract MyEntity getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract BadMultiplicity6InverseModel getBadMultiplicity6InverseModel();

    @ReferenceId
    int getBadMultiplicity6InverseModelId()
    {
      return 0;
    }
  }
}
