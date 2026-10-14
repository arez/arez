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
abstract class BadReferenceTypeInverseModel
{
  @Reference( inverse = Feature.ENABLE )
  abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  int getMyEntityId()
  {
    return 0;
  }

  @NullMarked
  @ArezComponent
  abstract static class OtherEntity
  {
    @Reference
    abstract @Nullable MyEntity getMyEntity();

    @ReferenceId
    int getMyEntityId()
    {
      return 0;
    }
  }

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Inverse
    abstract @Nullable Collection<OtherEntity> getBadReferenceTypeInverseModels();
  }
}
