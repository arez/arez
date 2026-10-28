package com.example.misplaced_annotation;

import arez.annotations.ActAsArezComponent;
import arez.annotations.ArezComponent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
@ComponentAndFrameworkModel.FrameworkView
abstract class ComponentAndFrameworkModel
{
  @ActAsArezComponent
  @Target( ElementType.TYPE )
  @interface FrameworkView
  {
  }
}
