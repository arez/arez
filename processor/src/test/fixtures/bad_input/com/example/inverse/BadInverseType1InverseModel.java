package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class BadInverseType1InverseModel
{
  @Inverse
  abstract @Nullable Collection<OtherEntity> getMyEntitys();

  @NullMarked
  @ArezComponent()
  abstract static class OtherEntity
  {
    @Reference( inverse = Feature.ENABLE )
    abstract @Nullable MyEntity getBadInverseType1InverseModel();

    @ReferenceId
    int getBadInverseType1InverseModelId()
    {
      return 0;
    }
  }

  @NullMarked
  @ArezComponent( allowEmpty = true )
  abstract static class MyEntity
  {
  }
}
