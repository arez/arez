package com.example.misplaced_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CompiledFrameworkAnnotations
{
  @arez.annotations.ActAsArezComponent
  @Target( ElementType.TYPE )
  public @interface FirstView
  {
  }

  @arez.annotations.ActAsArezComponent
  @Target( ElementType.TYPE )
  public @interface SecondView
  {
  }

  @Target( ElementType.ANNOTATION_TYPE )
  @interface ActAsArezComponent
  {
  }

  @ActAsArezComponent
  @Target( ElementType.TYPE )
  public @interface SimpleNameView
  {
  }
}
