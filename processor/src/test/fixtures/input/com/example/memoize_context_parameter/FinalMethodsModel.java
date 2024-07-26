package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;

@ArezComponent
abstract class FinalMethodsModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @Memoize
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }

  @SuppressWarnings( "Arez:FinalMethod" )
  @MemoizeContextParameter
  final String captureMyContextVar()
  {
    return "";
  }

  @SuppressWarnings( "Arez:FinalMethod" )
  final void pushMyContextVar( String var )
  {
  }

  @SuppressWarnings( "Arez:FinalMethod" )
  final void popMyContextVar( String var )
  {
  }
}
