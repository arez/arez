package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import arez.annotations.OnDeactivate;
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

  @OnDeactivate
  void onMyValueDeactivate()
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
