package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnActivate;
import java.text.ParseException;

@ArezComponent
public class OnActivateThrowsExceptionModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  void onMyValueActivate()
    throws ParseException
  {
  }
}
