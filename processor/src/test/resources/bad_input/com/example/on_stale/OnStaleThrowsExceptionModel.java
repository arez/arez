package com.example.on_stale;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnStale;
import java.text.ParseException;

@ArezComponent
public class OnStaleThrowsExceptionModel
{
  @Computed
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
