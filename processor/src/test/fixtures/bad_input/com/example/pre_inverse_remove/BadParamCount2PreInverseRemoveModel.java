package com.example.pre_inverse_remove;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PreInverseRemove;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class BadParamCount2PreInverseRemoveModel
{
  @PreInverseRemove
  void preElementsRemove( @NonNull final Element element1, @NonNull final Element element2 )
  {
  }

  @Inverse( referenceName = "other" )
  abstract @Nullable Collection<Element> getElements();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract @Nullable BadParamCount2PreInverseRemoveModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
