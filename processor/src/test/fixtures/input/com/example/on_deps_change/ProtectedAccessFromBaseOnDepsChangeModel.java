package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import com.example.on_deps_change.other.BaseProtectedAccessOnDepsChangeModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ProtectedAccessFromBaseOnDepsChangeModel
  extends BaseProtectedAccessOnDepsChangeModel
{
}
