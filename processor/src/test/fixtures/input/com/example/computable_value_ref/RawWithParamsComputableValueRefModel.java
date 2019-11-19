package com.example.computable_value_ref;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;

@ArezComponent
abstract class RawWithParamsComputableValueRefModel
{
  @Memoize
  public long getTime( final int zone )
  {
    return 0;
  }

  @SuppressWarnings( "rawtypes" )
  @ComputableValueRef
  abstract ComputableValue getTimeComputableValue( int zone );
}
