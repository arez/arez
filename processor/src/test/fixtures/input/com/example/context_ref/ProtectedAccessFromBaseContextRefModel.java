package com.example.context_ref;

import arez.annotations.ArezComponent;
import com.example.context_ref.other.BaseProtectedAccessContextRefModel;

@ArezComponent( allowEmpty = true )
abstract class ProtectedAccessFromBaseContextRefModel
  extends BaseProtectedAccessContextRefModel
{
}
