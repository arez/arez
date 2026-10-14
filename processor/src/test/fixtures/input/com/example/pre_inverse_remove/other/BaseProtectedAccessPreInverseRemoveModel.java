package com.example.pre_inverse_remove.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PreInverseRemove;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.pre_inverse_remove.ProtectedAccessFromBasePreInverseRemoveModel;
import java.util.Collection;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseProtectedAccessPreInverseRemoveModel
{
  @PreInverseRemove
  protected void preElementsRemove( @NonNull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  protected abstract @Nullable Collection<Element> getElements();

  @NullMarked
  @ArezComponent
  public abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract @Nullable ProtectedAccessFromBasePreInverseRemoveModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
