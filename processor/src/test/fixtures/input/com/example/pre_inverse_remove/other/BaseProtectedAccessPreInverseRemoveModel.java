package com.example.pre_inverse_remove.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PreInverseRemove;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.pre_inverse_remove.ProtectedAccessFromBasePreInverseRemoveModel;
import java.util.Collection;
import javax.annotation.Nonnull;

public abstract class BaseProtectedAccessPreInverseRemoveModel
{
  @PreInverseRemove
  protected void preElementsRemove( @Nonnull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  protected abstract Collection<Element> getElements();

  @ArezComponent
  public static abstract class Element
  {
    @Reference( inverse = Feature.ENABLE )
    protected abstract ProtectedAccessFromBasePreInverseRemoveModel getOther();

    @ReferenceId
    protected int getOtherId()
    {
      return 0;
    }
  }
}
