package com.example.component_ref;

import arez.annotations.ArezComponent;
import com.example.component_ref.other.BaseProtectedAccessComponentRefModel;

@ArezComponent( allowEmpty = true )
abstract class ProtectedAccessFromBaseComponentRefModel
  extends BaseProtectedAccessComponentRefModel
{
}
