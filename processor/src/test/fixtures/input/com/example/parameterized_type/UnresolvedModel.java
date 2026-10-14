package com.example.parameterized_type;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class UnresolvedModel<X extends Number>
  extends ParentModel<X>
{
}
