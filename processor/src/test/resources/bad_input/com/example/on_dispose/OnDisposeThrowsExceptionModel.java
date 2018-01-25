package com.example.on_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDispose;
import java.text.ParseException;

@ArezComponent
public abstract class OnDisposeThrowsExceptionModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDispose
  void onMyValueDispose()
    throws ParseException
  {
  }
}
