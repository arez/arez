package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;

@ArezComponent
abstract class PackageAccessOnActivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @OnActivate
  void onTimeActivate()
  {
  }
}
