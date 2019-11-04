package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;
import java.text.ParseException;

@ArezComponent
public abstract class OnDeactivateThrowsExceptionModel
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @SuppressWarnings( "RedundantThrows" )
  @OnDeactivate
  void onMyValueDeactivate()
    throws ParseException
  {
  }
}
