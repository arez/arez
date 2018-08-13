package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import java.util.Collection;

@ArezComponent
abstract class BadCollectionTypeInverseModel
{
  @Inverse
  abstract Collection<String> getMyEntity();
}
