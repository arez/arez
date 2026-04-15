package com.example.component_dependency;

import arez.annotations.ActAsArezComponent;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@ActAsArezComponent
@interface FrameworkView
{
}

@ArezComponent
abstract class ActAsArezComponentFieldDependencyModel
{
  @ComponentDependency
  final MyType time = null;

  @FrameworkView
  public interface MyType
  {
  }
}
