package com.example.misplaced_annotation;

import arez.annotations.ArezComponent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ComponentAndSimpleNameFrameworkModel.FrameworkView
@ArezComponent( allowEmpty = true )
abstract class ComponentAndSimpleNameFrameworkModel
{
  @Target( ElementType.ANNOTATION_TYPE )
  @interface ActAsArezComponent
  {
  }

  @ActAsArezComponent
  @Target( ElementType.TYPE )
  @interface FrameworkView
  {
  }
}
