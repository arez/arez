package com.example.misplaced_annotation;

import arez.annotations.ActAsArezComponent;
import arez.annotations.ArezComponent;
import arez.annotations.ArezComponentLike;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
@ArezComponentLike
@AllComponentAnnotationsModel.FrameworkView
abstract class AllComponentAnnotationsModel
{
  @ActAsArezComponent
  @Target( ElementType.TYPE )
  @interface FrameworkView
  {
  }
}
