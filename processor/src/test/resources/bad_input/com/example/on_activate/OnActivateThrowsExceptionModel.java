package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import java.text.ParseException;

@ArezComponent
public abstract class OnActivateThrowsExceptionModel
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @SuppressWarnings( "RedundantThrows" )
  @OnActivate
  void onMyValueActivate()
    throws ParseException
  {
  }
}
