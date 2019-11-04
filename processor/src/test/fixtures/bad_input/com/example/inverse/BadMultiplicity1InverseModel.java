package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class BadMultiplicity1InverseModel
{
  @Inverse
  abstract Collection<MyEntity> getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract BadMultiplicity1InverseModel getBadMultiplicity1InverseModel();

    @ReferenceId
    int getBadMultiplicity1InverseModelId()
    {
      return 0;
    }
  }
}
