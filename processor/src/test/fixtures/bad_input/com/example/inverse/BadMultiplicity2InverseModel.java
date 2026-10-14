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
abstract class BadMultiplicity2InverseModel
{
  @Inverse
  abstract @Nullable Collection<MyEntity> getMyEntity();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    abstract @Nullable BadMultiplicity2InverseModel getBadMultiplicity2InverseModel();

    @ReferenceId
    int getBadMultiplicity2InverseModelId()
    {
      return 0;
    }
  }
}
