package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.OnActivate;

@ArezComponent
public abstract class OnActivateNoMemoizeModel
{
  @OnActivate
  void onMyValueActivate()
  {
  }
}
