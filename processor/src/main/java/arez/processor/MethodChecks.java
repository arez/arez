package arez.processor;

import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import static arez.processor.ProcessorUtil.*;

final class MethodChecks
{
  private MethodChecks()
  {
  }

  /**
   * Verifies that the method is not final, static, abstract or private.
   * The intent is to verify that it can be overridden and wrapped in a sub-class in the same package.
   */
  static void mustBeWrappable( @Nonnull final TypeElement targetType,
                               @Nonnull final String annotationName,
                               @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    mustBeOverridable( targetType, annotationName, method );
    mustNotBeAbstract( annotationName, method );
  }

  /**
   * Verifies that the method is not final, static or abstract.
   * The intent is to verify that it can be overridden in sub-class in same package.
   */
  static void mustBeOverridable( @Nonnull final TypeElement targetType,
                                 @Nonnull final String annotationName,
                                 @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    mustNotBeFinal( annotationName, method );
    mustBeSubclassCallable( targetType, annotationName, method );
  }

  /**
   * Verifies that the method is not static, abstract or private.
   * The intent is to verify that it can be instance called by sub-class in same package.
   */
  static void mustBeSubclassCallable( @Nonnull final TypeElement targetType,
                                      @Nonnull final String annotationName,
                                      @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    mustNotBeStatic( annotationName, method );
    mustNotBePrivate( annotationName, method );
    mustNotBePackageAccessInDifferentPackage( targetType, annotationName, method );
  }

  /**
   * Verifies that the method follows conventions of a lifecycle hook.
   * The intent is to verify that it can be instance called by sub-class in same
   * package at a lifecycle stage. It should not raise errors, return values or accept
   * parameters.
   */
  static void mustBeLifecycleHook( @Nonnull final TypeElement targetType,
                                   @Nonnull final String annotationName,
                                   @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    mustNotBeAbstract( annotationName, method );
    mustBeSubclassCallable( targetType, annotationName, method );
    mustNotHaveAnyParameters( annotationName, method );
    mustNotReturnAnyValue( annotationName, method );
    mustNotThrowAnyExceptions( annotationName, method );
  }

  static void mustNotBePublic( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.PUBLIC ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                        " target must not be public", method );
    }
  }

  private static void mustNotBeStatic( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                        " target must not be static", method );
    }
  }

  static void mustNotBeAbstract( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                        " target must not be abstract", method );
    }
  }

  static void mustBeAbstract( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !method.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must be abstract",
                                        method );
    }
  }

  private static void mustNotBePrivate( @Nonnull final String annotationName,
                                        @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.PRIVATE ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                        " target must not be private", method );
    }
  }

  private static void mustNotBePackageAccessInDifferentPackage( @Nonnull final TypeElement component,
                                                                @Nonnull final String annotationName,
                                                                @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    final Set<Modifier> modifiers = method.getModifiers();
    final boolean isPackageAccess =
      !modifiers.contains( Modifier.PRIVATE ) &&
      !modifiers.contains( Modifier.PROTECTED ) &&
      !modifiers.contains( Modifier.PUBLIC );

    if ( isPackageAccess )
    {
      final PackageElement packageElement = getPackageElement( component );
      final PackageElement methodPackageElement = getPackageElement( (TypeElement) method.getEnclosingElement() );
      final Name componentPackageName = packageElement.getQualifiedName();
      final Name methodPackageName = methodPackageElement.getQualifiedName();
      if ( !Objects.equals( componentPackageName, methodPackageName ) )
      {
        throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must " +
                                          "not be package access if the method is in a different package from " +
                                          "the @ArezComponent", method );
      }
    }
  }

  private static void mustNotBeFinal( @Nonnull final String annotationName,
                                      @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                        " target must not be final", method );
    }
  }

  static void mustBeFinal( @Nonnull final String annotationName, @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !method.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) + " target must be final",
                                        method );
    }
  }

  static void mustNotHaveAnyParameters( @Nonnull final String annotationName,
                                        @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                        " target must not have any parameters", method );
    }
  }

  static void mustHaveParameters( @Nonnull final String annotationName,
                                  @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( method.getParameters().isEmpty() )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                        " target must have parameters", method );
    }
  }

  static void mustNotReturnAnyValue( @Nonnull final String annotationName,
                                     @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( TypeKind.VOID != method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                        " target must not return a value", method );
    }
  }

  static void mustReturnAValue( @Nonnull final String annotationName,
                                @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( TypeKind.VOID == method.getReturnType().getKind() )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                        " target must return a value", method );
    }
  }

  static void mustNotThrowAnyExceptions( @Nonnull final String annotationName,
                                         @Nonnull final ExecutableElement method )
    throws ArezProcessorException
  {
    if ( !method.getThrownTypes().isEmpty() )
    {
      throw new ArezProcessorException( "@" + ProcessorUtil.toSimpleName( annotationName ) +
                                        " target must not throw any exceptions", method );
    }
  }
}
