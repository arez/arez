package com.example.misplaced_annotation;

import arez.annotations.ArezComponentLike;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class NestedComponentLikeAndSimpleNameFrameworkModel
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

  @FrameworkView
  @ArezComponentLike
  abstract static class MyView
  {
  }
}
