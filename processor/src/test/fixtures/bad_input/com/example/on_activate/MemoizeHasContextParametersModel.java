package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import arez.annotations.OnActivate;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class MemoizeHasContextParametersModel
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  final void onMyValueActivate()
  {
  }

  @MemoizeContextParameter
  @Nullable String captureMyContextVar()
  {
    return "";
  }

  void pushMyContextVar( @Nullable String var )
  {
  }

  void popMyContextVar( @Nullable String var )
  {
  }
}
