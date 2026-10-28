package com.example.misplaced_annotation;

import arez.annotations.ActAsArezComponent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;

@NullMarked
@MultipleFrameworkAnnotationsModel.FrameworkView
@MultipleFrameworkAnnotationsModel.OtherFrameworkView
abstract class MultipleFrameworkAnnotationsModel
{
  @ActAsArezComponent
  @Target( ElementType.TYPE )
  @interface FrameworkView
  {
  }

  @ActAsArezComponent
  @Target( ElementType.TYPE )
  @interface OtherFrameworkView
  {
  }
}
