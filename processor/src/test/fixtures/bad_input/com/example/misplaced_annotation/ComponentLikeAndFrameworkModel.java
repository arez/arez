package com.example.misplaced_annotation;

import arez.annotations.ActAsArezComponent;
import arez.annotations.ArezComponentLike;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponentLike
@ComponentLikeAndFrameworkModel.FrameworkView
interface ComponentLikeAndFrameworkModel
{
  @ActAsArezComponent
  @Target( ElementType.TYPE )
  @interface FrameworkView
  {
  }
}
