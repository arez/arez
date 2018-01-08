package arez.processor;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;

final class MethodChecks
{
  private MethodChecks()
  {
  }

  /**
   * Verifies that the method is not final, static, abstract or private.
   * The intent is to verify that it can be overridden in sub-class in same package.
   */
  static void mustBeOverridable( @Nonnull final String annotationName,
                                 @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    mustNotBeFinal( annotationName, method );
    mustBeSubclassCallable( annotationName, method );
  }

  /**
   * Verifies that the method is not static, abstract or private.
   * The intent is to verify that it can be instance called by sub-class in same package.
   */
  static void mustBeSubclassCallable( @Nonnull final String annotationName,
                                      @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    mustNotBeStatic( annotationName, method );
    mustNotBePrivate( annotationName, method );
  }

  /**
   * Verifies that the method follows conventions of a lifecycle hook.
   * The intent is to verify that it can be instance called by sub-class in same
   * package at a lifecycle stage. It should not raise errors, return values or accept
   * parameters.
   */
  static void mustBeLifecycleHook( @Nonnull final String annotationName,
                                   @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    mustBeSubclassCallable( annotationName, method );
    mustNotHaveAnyParameters( annotationName, method );
    mustNotReturnAnyValue( annotationName, method );
    mustNotThrowAnyExceptions( annotationName, method );
  }

  private static void mustNotBeStatic( @Nonnull final String annotationName,
                                       @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must not be static", method );
    }
  }

  private static void mustNotBePrivate( @Nonnull final String annotationName,
                                        @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must not be private", method );
    }
  }

  private static void mustNotBeFinal( @Nonnull final String annotationName,
                                      @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must not be final", method );
    }
  }

  static void mustBeFinal( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must be final", method );
    }
  }

  static void mustNotHaveAnyParameters( @Nonnull final String annotationName,
                                        @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must not have any parameters", method );
    }
  }

  static void mustNotReturnAnyValue( @Nonnull final String annotationName,
                                     @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must not return a value", method );
    }
  }

  static void mustReturnAValue( @Nonnull final String annotationName,
                                @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( TypeKind.VOID == method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must return a value", method );
    }
  }

  static void mustNotThrowAnyExceptions( @Nonnull final String annotationName,
                                         @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !method.getThrownTypes().isEmpty() )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must not throw any exceptions", method );
    }
  }

}
