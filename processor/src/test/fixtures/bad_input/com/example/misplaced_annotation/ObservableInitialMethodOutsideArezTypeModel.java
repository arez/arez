package com.example.misplaced_annotation;

import arez.annotations.ObservableInitial;

final class ObservableInitialMethodOutsideArezTypeModel
{
  @ObservableInitial
  String getInitialName()
  {
    return "";
  }
}
