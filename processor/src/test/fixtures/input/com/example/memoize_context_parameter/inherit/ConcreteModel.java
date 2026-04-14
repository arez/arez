package com.example.memoize_context_parameter.inherit;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import com.example.memoize_context_parameter.inherit.subpkg.AbstractModel;

@ArezComponent
abstract class ConcreteModel
  extends AbstractModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @Memoize
  public long countFromConcrete( final long time, float someOtherParameter )
  {
    return time;
  }

  @MemoizeContextParameter
  String captureMyContextVar()
  {
    return "";
  }
}
