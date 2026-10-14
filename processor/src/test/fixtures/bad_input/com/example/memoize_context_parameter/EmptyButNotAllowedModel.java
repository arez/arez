package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class EmptyButNotAllowedModel
{
  @MemoizeContextParameter( name = "public" )
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
