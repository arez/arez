package com.example.pre_dispose;

import arez.annotations.ArezComponent;
import com.example.pre_dispose.other.BaseProtectedAccessPreDisposeModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ProtectedAccessFromBasePreDisposeModel
  extends BaseProtectedAccessPreDisposeModel
{
}
