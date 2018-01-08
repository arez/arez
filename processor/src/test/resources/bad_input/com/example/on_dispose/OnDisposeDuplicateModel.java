package com.example.on_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDispose;

@ArezComponent
public class OnDisposeDuplicateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDispose( name = "myValue" )
  void foo()
  {
  }

  @OnDispose
  void onMyValueDispose()
  {
  }
}
