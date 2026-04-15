package com.example.misplaced_annotation;

import arez.annotations.MemoizeContextParameter;

final class MemoizeContextParameterOutsideArezTypeModel
{
  @MemoizeContextParameter
  void captureName( final String name )
  {
  }
}
