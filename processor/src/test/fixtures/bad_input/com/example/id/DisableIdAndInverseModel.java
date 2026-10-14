package com.example.id;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( requireId = Feature.DISABLE )
abstract class DisableIdAndInverseModel
{
  @Inverse
  abstract @Nullable Collection<MyEntity> getMyEntitys();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract @Nullable DisableIdAndInverseModel getDisableIdAndInverseModel();

    @ReferenceId
    int getDisableIdAndInverseModelId()
    {
      return 0;
    }
  }
}
