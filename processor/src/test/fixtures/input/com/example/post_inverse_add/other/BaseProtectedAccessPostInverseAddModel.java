package com.example.post_inverse_add.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PostInverseAdd;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import com.example.post_inverse_add.ProtectedAccessFromBasePostInverseAddModel;
import java.util.Collection;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class BaseProtectedAccessPostInverseAddModel
{
  @PostInverseAdd
  protected void postElementsAdd( @NonNull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  protected abstract @Nullable Collection<Element> getElements();

  @NullMarked
  @ArezComponent
  public abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract @Nullable ProtectedAccessFromBasePostInverseAddModel getOther();

    @ReferenceId
    int OtherId()
    {
      return 0;
    }
  }
}
