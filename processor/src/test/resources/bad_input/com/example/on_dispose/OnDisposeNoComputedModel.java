package com.example.on_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.OnDispose;

@ArezComponent
public class OnDisposeNoComputedModel
{
  @OnDispose
  void onMyValueDispose()
  {
  }
}
