package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;

@ArezComponent
abstract class PublicAccessViaInterfaceOnDeactivateModel
  implements OnDeactivateInterface
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @Override
  @OnDeactivate
  public void onTimeDeactivate()
  {
  }
}
