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
abstract class DuplicateInverseModel
{
  @Inverse
  abstract @Nullable Collection<MyEntity> getMyEntity();

  @Inverse( name = "myEntity" )
  abstract @Nullable Collection<MyEntity> getMyEntity2();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract @Nullable DuplicateInverseModel getDuplicateInverseModel();

    @ReferenceId
    int getDuplicateInverseModelId()
    {
      return 0;
    }
  }
}
