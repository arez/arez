package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;

@ArezComponent
abstract class BadType3InverseModel
{
  @Inverse
  abstract String getMyEntity();
}
