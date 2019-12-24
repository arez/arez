package com.example.post_inverse_add;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.PostInverseAdd;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nonnull;

@ArezComponent
abstract class SingularInversePostInverseAddModel
{
  @PostInverseAdd
  void postElementAdd( @Nonnull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  @Nonnull
  abstract Element getElement();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE, inverseMultiplicity = Multiplicity.ONE )
    abstract SingularInversePostInverseAddModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
