package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.OnActivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class OnActivateNoMemoizeModel
{
  @OnActivate
  void onMyValueActivate()
  {
  }
}
