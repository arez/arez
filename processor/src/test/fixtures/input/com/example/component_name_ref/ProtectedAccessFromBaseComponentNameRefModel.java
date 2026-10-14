package com.example.component_name_ref;

import arez.annotations.ArezComponent;
import com.example.component_name_ref.other.BaseProtectedAccessComponentNameRefModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ProtectedAccessFromBaseComponentNameRefModel
  extends BaseProtectedAccessComponentNameRefModel
{
}
