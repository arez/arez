package com.example.misplaced_annotation;

import arez.annotations.ActAsArezComponent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@ActAsArezComponent
@interface PublicCtorActAsArezComponentFrameworkView
{
}

@PublicCtorActAsArezComponentFrameworkView
abstract class PublicCtorActAsArezComponentModel
{
  public PublicCtorActAsArezComponentModel()
  {
  }
}
