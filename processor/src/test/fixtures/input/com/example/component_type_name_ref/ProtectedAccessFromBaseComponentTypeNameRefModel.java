package com.example.component_type_name_ref;

import arez.annotations.ArezComponent;
import com.example.component_type_name_ref.other.BaseProtectedAccessComponentTypeNameRefModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class ProtectedAccessFromBaseComponentTypeNameRefModel
  extends BaseProtectedAccessComponentTypeNameRefModel
{
}
