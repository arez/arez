package com.example.misplaced_annotation;

import arez.annotations.SuppressArezWarnings;

final class SuppressArezWarningsMethodOutsideArezTypeModel
{
  @SuppressArezWarnings( "Arez:PublicField" )
  void suppress()
  {
  }
}
