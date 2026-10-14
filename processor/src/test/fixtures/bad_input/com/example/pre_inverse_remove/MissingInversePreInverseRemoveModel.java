package com.example.pre_inverse_remove;

import arez.annotations.ArezComponent;
import arez.annotations.PreInverseRemove;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class MissingInversePreInverseRemoveModel
{
  @PreInverseRemove
  final void preElementRemove( @NonNull final Object element )
  {
  }
}
