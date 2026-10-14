package com.example.misplaced_annotation;

import arez.annotations.Action;
import arez.annotations.ActAsArezComponent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@ActAsArezComponent
@interface FrameworkView
{
}

@NullMarked
@FrameworkView
abstract class ActAsArezComponentUsageModel
{
  @Action
  void perform()
  {
  }
}
