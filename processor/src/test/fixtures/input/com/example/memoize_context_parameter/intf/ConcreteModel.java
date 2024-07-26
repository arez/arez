package com.example.memoize_context_parameter.intf;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.MemoizeContextParameter;
import com.example.memoize_context_parameter.intf.subpkg.MyInterfaceModelBase;

@ArezComponent
abstract class ConcreteModel
  implements MyInterfaceModelBase
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
