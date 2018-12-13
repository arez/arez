package arez.processor;

import com.google.auto.common.GeneratedAnnotationSpecs;
import com.squareup.javapoet.TypeSpec;
import javax.annotation.Nonnull;

final class Generator
{
  private Generator()
  {
  }

  static void addGeneratedAnnotation( @Nonnull final ComponentDescriptor descriptor,
                                      @Nonnull final TypeSpec.Builder builder )
  {
    GeneratedAnnotationSpecs
      .generatedAnnotationSpec( descriptor.getElements(), descriptor.getSourceVersion(), ArezProcessor.class )
      .ifPresent( builder::addAnnotation );
  }
}
