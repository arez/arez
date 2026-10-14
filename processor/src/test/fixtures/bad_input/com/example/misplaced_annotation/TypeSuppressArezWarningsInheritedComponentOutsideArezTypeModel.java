package com.example.misplaced_annotation;

import arez.annotations.ArezComponent;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressArezWarnings( "Arez:PublicField" )
abstract class TypeSuppressArezWarningsInheritedComponentOutsideArezTypeModelBase
{
}

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class TypeSuppressArezWarningsInheritedComponentOutsideArezTypeModel
  extends TypeSuppressArezWarningsInheritedComponentOutsideArezTypeModelBase
{
}
