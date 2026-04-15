package com.example.misplaced_annotation;

import arez.annotations.Reference;

final class ReferenceOutsideArezTypeModel
{
  @Reference
  Object getReference()
  {
    return null;
  }
}
