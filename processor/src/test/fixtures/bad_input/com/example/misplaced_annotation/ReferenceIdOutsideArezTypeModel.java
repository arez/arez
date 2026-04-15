package com.example.misplaced_annotation;

import arez.annotations.ReferenceId;

final class ReferenceIdOutsideArezTypeModel
{
  @ReferenceId
  Object getReferenceId()
  {
    return null;
  }
}
