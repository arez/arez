package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nullable;

@ArezComponent
abstract class BadReferenceMultiplicity6InverseModel
{
  @Reference( inverseMultiplicity = Multiplicity.ONE )
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
    @Nullable
    abstract BadReferenceMultiplicity6InverseModel getBadReferenceMultiplicity6InverseModel();
  }
}
