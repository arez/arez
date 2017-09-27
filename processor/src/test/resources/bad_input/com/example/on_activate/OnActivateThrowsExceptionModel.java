package com.example.on_activate;

import java.text.ParseException;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.OnActivate;

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
