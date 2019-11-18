package com.example.component_id_ref;

import arez.annotations.ArezComponent;
import com.example.component_id_ref.other.BaseProtectedAccessComponentIdRefModel;

@ArezComponent( allowEmpty = true )
abstract class ProtectedAccessFromBaseComponentIdRefModel
  extends BaseProtectedAccessComponentIdRefModel
{
}
