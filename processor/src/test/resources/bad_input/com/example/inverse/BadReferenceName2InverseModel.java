package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class BadReferenceName2InverseModel
{
  @Inverse( referenceName = "long" )
  abstract Collection<MyEntity> getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract BadReferenceName2InverseModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
