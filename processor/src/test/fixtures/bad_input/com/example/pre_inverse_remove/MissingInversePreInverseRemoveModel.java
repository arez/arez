package com.example.pre_inverse_remove;

import arez.annotations.ArezComponent;
import arez.annotations.PreInverseRemove;
import javax.annotation.Nonnull;

@ArezComponent( allowEmpty = true )
abstract class MissingInversePreInverseRemoveModel
{
  @PreInverseRemove
  final void preElementRemove( @Nonnull final Object element )
  {
  }
}
