package com.example.misplaced_annotation;

import arez.annotations.ArezComponent;
import arez.annotations.SuppressArezWarnings;

@SuppressArezWarnings( "Arez:PublicField" )
abstract class TypeSuppressArezWarningsInheritedComponentOutsideArezTypeModelBase
{
}

@ArezComponent( allowEmpty = true )
abstract class TypeSuppressArezWarningsInheritedComponentOutsideArezTypeModel
  extends TypeSuppressArezWarningsInheritedComponentOutsideArezTypeModelBase
{
}
