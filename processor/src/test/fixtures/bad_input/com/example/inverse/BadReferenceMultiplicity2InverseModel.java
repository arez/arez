package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class BadReferenceMultiplicity2InverseModel
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
    abstract Collection<BadReferenceMultiplicity2InverseModel> getBadReferenceMultiplicity2InverseModel();
  }
}
