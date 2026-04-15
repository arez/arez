package com.example.misplaced_annotation;

import arez.annotations.ComponentIdRef;

final class ComponentIdRefOutsideArezTypeModel
{
  @ComponentIdRef
  Object getIdObservable()
  {
    return null;
  }
}
