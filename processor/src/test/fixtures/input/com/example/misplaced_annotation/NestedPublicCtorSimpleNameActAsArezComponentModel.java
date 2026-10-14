package com.example.misplaced_annotation;

import arez.annotations.Action;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class NestedPublicCtorSimpleNameActAsArezComponentModel
{
  @NullMarked
  @Retention( RetentionPolicy.RUNTIME )
  @Target( ElementType.ANNOTATION_TYPE )
  @interface ActAsArezComponent
  {
  }

  @NullMarked
  @Retention( RetentionPolicy.RUNTIME )
  @Target( ElementType.TYPE )
  @ActAsArezComponent
  @interface FrameworkView
  {
  }

  @NullMarked
  @FrameworkView
  abstract static class MyView
  {
    public MyView()
    {
    }

    @Action
    void perform()
    {
    }
  }
}
