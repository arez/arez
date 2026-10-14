package com.example.pre_inverse_remove;

import arez.annotations.PreInverseRemove;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PreInverseRemoveInterface
{
  @PreInverseRemove
  default void preElementsRemove( final PublicAccessViaInterfacePreInverseRemoveModel.@NonNull Element element )
  {
  }
}
