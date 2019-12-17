package com.example.pre_inverse_remove;

import arez.annotations.PreInverseRemove;
import javax.annotation.Nonnull;

public interface PreInverseRemoveInterface
{
  @PreInverseRemove
  default void preElementsRemove( @Nonnull final PublicAccessViaInterfacePreInverseRemoveModel.Element element )
  {
  }
}
