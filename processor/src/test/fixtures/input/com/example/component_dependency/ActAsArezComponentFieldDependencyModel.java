package com.example.component_dependency;

import arez.annotations.ActAsArezComponent;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
@ActAsArezComponent
@interface FrameworkView
{
}

@NullMarked
@ArezComponent
abstract class ActAsArezComponentFieldDependencyModel
{
  @ComponentDependency( validateTypeAtRuntime = true )
  final @Nullable MyType time = null;

  @NullMarked
  @FrameworkView
  public interface MyType
  {
  }
}
