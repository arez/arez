package com.example.misplaced_annotation;

import arez.annotations.Action;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

@FrameworkView
abstract class SimpleNameActAsArezComponentUsageModel
{
  @Action
  void perform()
  {
  }
}
