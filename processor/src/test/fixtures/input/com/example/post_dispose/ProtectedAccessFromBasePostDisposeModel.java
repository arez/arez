package com.example.post_dispose;

import arez.annotations.ArezComponent;
import com.example.post_dispose.other.BaseProtectedAccessPostDisposeModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ProtectedAccessFromBasePostDisposeModel
  extends BaseProtectedAccessPostDisposeModel
{
}
