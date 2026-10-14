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
abstract class BadMultiplicity1InverseModel
{
  @Inverse
  abstract @Nullable Collection<MyEntity> getMyEntity();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract @Nullable BadMultiplicity1InverseModel getBadMultiplicity1InverseModel();

    @ReferenceId
    int getBadMultiplicity1InverseModelId()
    {
      return 0;
    }
  }
}
