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
abstract class PrivateInverseModel
{
  @Inverse
  private @Nullable Collection<MyEntity> getMyEntity()
  {
    return null;
  }

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract @Nullable PrivateInverseModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
