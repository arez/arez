package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class BadName2InverseModel
{
  @Inverse( name = "long" )
  abstract @Nullable Collection<MyEntity> getMyEntity();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract @Nullable BadName2InverseModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
