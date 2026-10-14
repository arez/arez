package com.example.memoize_context_parameter;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class FullyAnnotatedBasicModel
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

  @Memoize
  public long count2( final long time, float someOtherParameter )
  {
    return time;
  }

  @MemoizeContextParameter( name = "CV", pattern = "count|getTime" )
  @Nullable String someMethod()
  {
    return "";
  }

  @MemoizeContextParameter( name = "CV", pattern = "count|getTime" )
  void pushSomeOtherMethod( @Nullable String var )
  {
  }

  @MemoizeContextParameter( name = "CV", pattern = "count|getTime" )
  void popSomeThirdMethod( @Nullable String var )
  {
  }
}
