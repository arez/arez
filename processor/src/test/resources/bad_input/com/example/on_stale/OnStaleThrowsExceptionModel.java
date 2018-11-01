package com.example.on_stale;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnStale;
import java.text.ParseException;

@ArezComponent
public abstract class OnStaleThrowsExceptionModel
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @OnStale
  void onMyValueStale()
    throws ParseException
  {
  }
}
