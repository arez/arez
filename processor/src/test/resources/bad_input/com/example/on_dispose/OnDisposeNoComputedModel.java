package com.example.on_dispose;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnDispose;

@ArezComponent
public class OnDisposeNoComputedModel
{
  @OnDispose
  void onMyValueDispose()
  {
  }
}
