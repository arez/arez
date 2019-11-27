package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;

@ArezComponent
abstract class PublicAccessViaInterfaceOnActivateModel
  implements OnActivateInterface
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @Override
  @OnActivate
  public void onTimeActivate()
  {
  }
}
