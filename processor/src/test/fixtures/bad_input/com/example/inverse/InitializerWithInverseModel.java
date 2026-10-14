package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class InitializerWithInverseModel
{
  @Inverse
  @NonNull
  @Observable( initializer = Feature.ENABLE )
  abstract MyEntity getMyEntity();

  @NullMarked
  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract @Nullable InitializerWithInverseModel getInitializerWithInverseModel();

    @ReferenceId
    abstract int getInitializerWithInverseModelId();
  }
}
