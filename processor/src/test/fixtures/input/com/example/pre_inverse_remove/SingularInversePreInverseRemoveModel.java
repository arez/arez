package com.example.pre_inverse_remove;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.PreInverseRemove;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;

@ArezComponent
abstract class SingularInversePreInverseRemoveModel
{
  @PreInverseRemove
  void preElementRemove( @Nonnull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  @Nonnull
  abstract Element getElement();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE, inverseMultiplicity = Multiplicity.ONE )
    abstract SingularInversePreInverseRemoveModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
