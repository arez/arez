package com.example.post_inverse_add.other;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.Multiplicity;
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

  @Inverse( referenceName = "requiredOwner" )
  protected abstract Element getRequired();

  @Inverse( referenceName = "optionalOwner" )
  protected abstract @Nullable Element getOptional();

  @NullMarked
  @ArezComponent
  protected abstract static class Element
  {
    Element()
    {
    }

    @Reference( inverse = Feature.ENABLE )
    abstract @Nullable ProtectedAccessFromBasePostInverseAddModel getOther();

    @ReferenceId
    int OtherId()
    {
      return 0;
    }

    @Reference( inverseName = "required", inverseMultiplicity = Multiplicity.ONE )
    abstract @Nullable ProtectedAccessFromBasePostInverseAddModel getRequiredOwner();

    @ReferenceId
    int getRequiredOwnerId()
    {
      return 0;
    }

    @Reference( inverseName = "optional", inverseMultiplicity = Multiplicity.ZERO_OR_ONE )
    abstract @Nullable ProtectedAccessFromBasePostInverseAddModel getOptionalOwner();

    @ReferenceId
    int getOptionalOwnerId()
    {
      return 0;
    }
  }
}
