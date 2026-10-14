package com.example.misplaced_annotation;

import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class SuppressArezWarningsMethodOutsideArezTypeModel
{
  @SuppressArezWarnings( "Arez:PublicField" )
  void suppress()
  {
  }
}
