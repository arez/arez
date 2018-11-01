package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class OnDeactivateNoMemoizeModel
{
  @OnDeactivate
  void onMyValueDeactivate()
  {
  }
}
