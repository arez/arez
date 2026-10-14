package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.OnDeactivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class OnDeactivateNoMemoizeModel
{
  @OnDeactivate
  void onMyValueDeactivate()
  {
  }
}
