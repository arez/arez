package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;

@ArezComponent
abstract class BadReferenceMultiplicity4InverseModel
{
  @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
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
    abstract BadReferenceMultiplicity4InverseModel getBadReferenceMultiplicity4InverseModel();
  }
}
