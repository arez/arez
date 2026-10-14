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
abstract class Suppressed1ProtectedAccessPreInverseRemoveModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedMethod" )
  @PreInverseRemove
  protected void preElementsRemove( @NonNull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  abstract @Nullable Collection<Element> getElements();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract @Nullable Suppressed1ProtectedAccessPreInverseRemoveModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
