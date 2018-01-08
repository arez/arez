package com.example.parameterized_type;

import arez.annotations.ArezComponent;

@ArezComponent
class UnresolvedModel<X extends Number>
  extends ParentModel<X>
{
}
