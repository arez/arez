package com.example.parameterized_type;

import arez.annotations.ArezComponent;

@ArezComponent
abstract class UnresolvedModel<X extends Number>
  extends ParentModel<X>
{
}
