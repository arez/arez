package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.MemoizeContextParameter;

@ArezComponent
abstract class EmptyButNotAllowedModel
{
  @MemoizeContextParameter( name = "public" )
  String captureMyContextVar()
  {
    return "";
  }

  void pushMyContextVar( String var )
  {
  }

  void popMyContextVar( String var )
  {
  }
}
