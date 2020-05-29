package com.example.id;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent( requireId = Feature.DISABLE )
abstract class DisableIdAndInverseModel
{
  @Inverse
  abstract Collection<MyEntity> getMyEntitys();

  @ArezComponent
  abstract static class MyEntity
  {
    @Reference( inverseMultiplicity = Multiplicity.MANY )
    abstract DisableIdAndInverseModel getDisableIdAndInverseModel();

    @ReferenceId
    int getDisableIdAndInverseModelId()
    {
      return 0;
    }
  }
}
