package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;

@ArezComponent
abstract class InitializerWithInverseModel
{
  @Inverse
  @Nonnull
  @Observable( initializer = Feature.ENABLE )
  abstract MyEntity getMyEntity();

  @ArezComponent
  static abstract class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.ONE )
    abstract InitializerWithInverseModel getInitializerWithInverseModel();

    @ReferenceId
    abstract int getInitializerWithInverseModelId();
  }
}
