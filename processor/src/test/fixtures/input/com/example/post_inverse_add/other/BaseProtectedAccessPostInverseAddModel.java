package com.example.post_inverse_add.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PostInverseAdd;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.post_inverse_add.ProtectedAccessFromBasePostInverseAddModel;
import java.util.Collection;
import javax.annotation.Nonnull;

public abstract class BaseProtectedAccessPostInverseAddModel
{
  @PostInverseAdd
  protected void postElementsAdd( @Nonnull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  protected abstract Collection<Element> getElements();

  @ArezComponent
  public static abstract class Element
  {
    @Reference( inverse = Feature.ENABLE )
    protected abstract ProtectedAccessFromBasePostInverseAddModel getOther();

    @ReferenceId
    protected int OtherId()
    {
      return 0;
    }
  }
}
