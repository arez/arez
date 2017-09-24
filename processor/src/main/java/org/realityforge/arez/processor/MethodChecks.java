package org.realityforge.arez.processor;

import java.lang.annotation.Annotation;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;

final class MethodChecks
{
  private MethodChecks()
  {
  }

  static void mustNotBeStatic( @Nonnull final Class<? extends Annotation> type,
                               @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@" + type.getSimpleName() + " target must not be static", method );
    }
  }

  static void mustNotBeAbstract( @Nonnull final Class<? extends Annotation> type,
                                 @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ArezProcessorException( "@" + type.getSimpleName() + " target must not be abstract", method );
    }
  }

  static void mustNotBePrivate( @Nonnull final Class<? extends Annotation> type,
                                @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@" + type.getSimpleName() + " target must not be private", method );
    }
  }

  static void mustNotBeFinal( @Nonnull final Class<? extends Annotation> type, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@" + type.getSimpleName() + " target must not be final", method );
    }
  }

  static void mustBeFinal( @Nonnull final Class<? extends Annotation> type, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@" + type.getSimpleName() + " target must be final", method );
    }
  }

  static void mustNotHaveAnyParameters( @Nonnull final Class<? extends Annotation> type,
                                        @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@" + type.getSimpleName() + " target must not have any parameters", method );
    }
  }

  static void mustNotReturnAnyValue( @Nonnull final Class<? extends Annotation> type,
                                     @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@" + type.getSimpleName() + " target must not return a value", method );
    }
  }

  static void mustReturnAValue( @Nonnull final Class<? extends Annotation> type,
                                @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( TypeKind.VOID == method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@" + type.getSimpleName() + " target must return a value", method );
    }
  }

  static void mustNotThrowAnyExceptions( @Nonnull final Class<? extends Annotation> type,
                                         @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !method.getThrownTypes().isEmpty() )
    {
      throw new ArezProcessorException( "@" + type.getSimpleName() + " target must not throw any exceptions", method );
    }
  }
}
