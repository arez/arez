package com.example.misplaced_annotation;

import arez.annotations.ObservableValueRef;

final class ObservableValueRefOutsideArezTypeModel
{
  @ObservableValueRef
  Object getNameObservableValue()
  {
    return null;
  }
}
