package com.example.on_stale;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.OnStale;

@ArezComponent
public class OnStaleNoComputedModel
{
  @OnStale
  void onMyValueStale()
  {
  }
}
