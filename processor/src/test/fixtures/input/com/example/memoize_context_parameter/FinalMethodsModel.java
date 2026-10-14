package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
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
  final @Nullable String captureMyContextVar()
  {
    return "";
  }

  @SuppressWarnings( "Arez:FinalMethod" )
  final void pushMyContextVar( @Nullable String var )
  {
  }

  @SuppressWarnings( "Arez:FinalMethod" )
  final void popMyContextVar( @Nullable String var )
  {
  }
}
