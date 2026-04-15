package com.example.misplaced_annotation;

import arez.annotations.Action;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SimpleNameActAsArezComponentUsageModel.FrameworkView
abstract class SimpleNameActAsArezComponentUsageModel
{
  @Retention( RetentionPolicy.RUNTIME )
  @Target( ElementType.ANNOTATION_TYPE )
  @interface ActAsArezComponent
  {
  }

  @Retention( RetentionPolicy.RUNTIME )
  @Target( ElementType.TYPE )
  @ActAsArezComponent
  @interface FrameworkView
  {
  }

  @Action
  void perform()
  {
  }
}
