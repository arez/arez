package arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

final class ArezUtils
{
  private ArezUtils()
  {
  }

  static void mustBeStandardRefMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                       @Nonnull final ComponentDescriptor descriptor,
                                       @Nonnull final ExecutableElement method,
                                       @Nonnull final String annotationClassname )
  {
    mustBeRefMethod( descriptor, method, annotationClassname );
    MemberChecks.mustNotHaveAnyParameters( annotationClassname, method );
    mustBeInternalRefMethod( processingEnv, descriptor, method, annotationClassname );
  }

  static void mustBeRefMethod( @Nonnull final ComponentDescriptor descriptor,
                               @Nonnull final ExecutableElement method,
                               @Nonnull final String annotationClassname )
  {
    MemberChecks.mustBeAbstract( annotationClassname, method );
    final TypeElement typeElement = descriptor.getElement();
    MemberChecks.mustNotBePackageAccessInDifferentPackage( typeElement,
                                                           Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                                           annotationClassname,
                                                           method );
    MemberChecks.mustReturnAValue( annotationClassname, method );
    MemberChecks.mustNotThrowAnyExceptions( annotationClassname, method );
  }

  static void mustBeInternalRefMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                       @Nonnull final ComponentDescriptor descriptor,
                                       @Nonnull final ExecutableElement method,
                                       @Nonnull final String annotationClassname )
  {
    final TypeElement typeElement = descriptor.getElement();
    if ( doesMethodNotOverrideInterfaceMethod( processingEnv, typeElement, method ) )
    {
      MemberChecks.shouldNotBePublic( processingEnv,
                                      method,
                                      annotationClassname,
                                      Constants.WARNING_PUBLIC_REF_METHOD,
                                      Constants.SUPPRESS_AREZ_WARNINGS_ANNOTATION_CLASSNAME );
    }
    if ( Objects.equals( typeElement, method.getEnclosingElement() ) )
    {
      MemberChecks.shouldNotBeProtected( processingEnv,
                                         method,
                                         annotationClassname,
                                         Constants.WARNING_PROTECTED_REF_METHOD,
                                         Constants.SUPPRESS_AREZ_WARNINGS_ANNOTATION_CLASSNAME );
    }
  }

  private static boolean doesMethodNotOverrideInterfaceMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                                               @Nonnull final TypeElement typeElement,
                                                               @Nonnull final ExecutableElement method )
  {
    return !ProcessorUtil.doesMethodOverrideInterfaceMethod( processingEnv.getTypeUtils(), typeElement, method );
  }
}
