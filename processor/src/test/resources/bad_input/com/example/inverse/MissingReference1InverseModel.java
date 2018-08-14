package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class MissingReference1InverseModel
{
  @Inverse
  abstract Collection<MyEntity> getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( name = "NotMatch", inverseMultiplicity = Multiplicity.MANY )
    abstract MissingReference1InverseModel getMissingReference1InverseModel();

    @ReferenceId( name = "NotMatch" )
    int getMissingReference1InverseModelId()
    {
      return 0;
    }
  }
}
