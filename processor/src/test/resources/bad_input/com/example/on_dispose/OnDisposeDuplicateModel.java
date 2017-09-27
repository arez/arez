package com.example.on_dispose;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.OnDispose;

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
