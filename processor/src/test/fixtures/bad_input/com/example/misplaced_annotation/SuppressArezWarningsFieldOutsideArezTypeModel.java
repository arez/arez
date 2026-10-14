package com.example.misplaced_annotation;

import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class SuppressArezWarningsFieldOutsideArezTypeModel
{
  @SuppressArezWarnings( "Arez:PublicField" )
  final @Nullable Object _dependency = null;
}
