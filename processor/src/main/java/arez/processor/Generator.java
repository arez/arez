package arez.processor;

import com.squareup.javapoet.ClassName;

final class Generator
{
  static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );

  private Generator()
  {
  }
}
