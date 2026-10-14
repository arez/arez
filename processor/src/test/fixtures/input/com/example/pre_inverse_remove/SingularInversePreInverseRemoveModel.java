package com.example.pre_inverse_remove;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
import arez.annotations.PreInverseRemove;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class SingularInversePreInverseRemoveModel
{
  @PreInverseRemove
  void preElementRemove( @NonNull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  @NonNull
  abstract Element getElement();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE, inverseMultiplicity = Multiplicity.ONE )
    abstract @Nullable SingularInversePreInverseRemoveModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
