package com.example.misplaced_annotation;

import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressArezWarnings( "Arez:PublicField" )
abstract class TypeSuppressArezWarningsOutsideArezTypeModel
{
}
