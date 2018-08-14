package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;

@ArezComponent
abstract class BadReferenceMultiplicity3InverseModel
{
  @Reference( inverseMultiplicity = Multiplicity.MANY )
  abstract MyEntity getMyEntity();

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  @ArezComponent
  static abstract class MyEntity
  {
    @Inverse
    @Nonnull
    abstract BadReferenceMultiplicity3InverseModel getBadReferenceMultiplicity3InverseModels();
  }
}
