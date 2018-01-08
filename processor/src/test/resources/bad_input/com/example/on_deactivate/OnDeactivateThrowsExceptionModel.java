package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDeactivate;
import java.text.ParseException;

@ArezComponent
public class OnDeactivateThrowsExceptionModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate
  void onMyValueDeactivate()
    throws ParseException
  {
  }
}
