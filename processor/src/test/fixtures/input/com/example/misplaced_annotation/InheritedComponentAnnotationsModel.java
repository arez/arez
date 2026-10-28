package com.example.misplaced_annotation;

import arez.annotations.Action;
import arez.annotations.ActAsArezComponent;
import arez.annotations.ArezComponent;
import arez.annotations.ArezComponentLike;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponentLike
final class InheritedComponentAnnotationsModel
{
  @ActAsArezComponent
  @Inherited
  @Retention( RetentionPolicy.RUNTIME )
  @Target( ElementType.TYPE )
  @interface FrameworkView
  {
  }

  @ArezComponentLike
  abstract static class ComponentLikeBase
  {
    @Action
    void perform()
    {
    }
  }

  @FrameworkView
  abstract static class FrameworkSubclass extends ComponentLikeBase
  {
  }

  @ArezComponent
  abstract static class ComponentSubclass extends FrameworkSubclass
  {
  }

  @ArezComponentLike
  abstract static class ComponentLikeSubclass extends FrameworkSubclass
  {
  }

  @ArezComponentLike
  interface ComponentLikeInterface
  {
    @Action
    default void perform()
    {
    }
  }

  @ArezComponent
  interface ComponentInterface extends ComponentLikeInterface
  {
  }

  @FrameworkView
  interface FrameworkInterface extends ComponentLikeInterface
  {
  }
}
