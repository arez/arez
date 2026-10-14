package com.example.component_ref;

import arez.annotations.ArezComponent;
import com.example.component_ref.other.BaseProtectedAccessComponentRefModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ProtectedAccessFromBaseComponentRefModel
  extends BaseProtectedAccessComponentRefModel
{
}
