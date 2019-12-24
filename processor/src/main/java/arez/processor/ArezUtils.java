package arez.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.realityforge.proton.AnnotationsUtil;
import org.realityforge.proton.MemberChecks;
import org.realityforge.proton.ProcessorException;

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
    shouldBeInternalRefMethod( processingEnv, descriptor, method, annotationClassname );
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

  static void mustBeHookHook( @Nonnull final TypeElement targetType,
                              @Nonnull final String annotationName,
                              @Nonnull final ExecutableElement method )
    throws ProcessorException
  {
    MemberChecks.mustNotBeAbstract( annotationName, method );
    MemberChecks.mustBeSubclassCallable( targetType, Constants.COMPONENT_ANNOTATION_CLASSNAME, annotationName, method );
    MemberChecks.mustNotReturnAnyValue( annotationName, method );
    MemberChecks.mustNotThrowAnyExceptions( annotationName, method );
  }

  static void shouldBeInternalRefMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                         @Nonnull final ComponentDescriptor descriptor,
                                         @Nonnull final ExecutableElement method,
                                         @Nonnull final String annotationClassname )
  {
    MemberChecks.shouldBeInternalMethod( processingEnv,
                                         descriptor.getElement(),
                                         method,
                                         annotationClassname,
                                         Constants.WARNING_PUBLIC_REF_METHOD,
                                         Constants.WARNING_PROTECTED_REF_METHOD,
                                         Constants.SUPPRESS_AREZ_WARNINGS_ANNOTATION_CLASSNAME );
  }

  static void shouldBeInternalLifecycleMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                               @Nonnull final ComponentDescriptor descriptor,
                                               @Nonnull final ExecutableElement method,
                                               @Nonnull final String annotationClassname )
  {
    MemberChecks.shouldBeInternalMethod( processingEnv,
                                         descriptor.getElement(),
                                         method,
                                         annotationClassname,
                                         Constants.WARNING_PUBLIC_LIFECYCLE_METHOD,
                                         Constants.WARNING_PROTECTED_LIFECYCLE_METHOD,
                                         Constants.SUPPRESS_AREZ_WARNINGS_ANNOTATION_CLASSNAME );
  }

  static void shouldBeInternalHookMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                          @Nonnull final ComponentDescriptor descriptor,
                                          @Nonnull final ExecutableElement method,
                                          @Nonnull final String annotationClassname )
  {
    MemberChecks.shouldBeInternalMethod( processingEnv,
                                         descriptor.getElement(),
                                         method,
                                         annotationClassname,
                                         Constants.WARNING_PUBLIC_HOOK_METHOD,
                                         Constants.WARNING_PROTECTED_HOOK_METHOD,
                                         Constants.SUPPRESS_AREZ_WARNINGS_ANNOTATION_CLASSNAME );
  }

  @Nullable
  public static String deriveName( @Nonnull final ExecutableElement method,
                                   @Nonnull final Pattern pattern,
                                   @Nonnull final String name )
    throws ProcessorException
  {
    if ( Constants.SENTINEL.equals( name ) )
    {
      final String methodName = method.getSimpleName().toString();
      final Matcher matcher = pattern.matcher( methodName );
      if ( matcher.find() )
      {
        final String candidate = matcher.group( 1 );
        return firstCharacterToLowerCase( candidate );
      }
      else
      {
        return null;
      }
    }
    else
    {
      return name;
    }
  }

  @Nonnull
  public static String firstCharacterToLowerCase( @Nonnull final String name )
  {
    return Character.toLowerCase( name.charAt( 0 ) ) + name.substring( 1 );
  }

  public static boolean isDisposableTrackableRequired( @Nonnull final Element element )
  {
    final VariableElement variableElement = (VariableElement)
      AnnotationsUtil.getAnnotationValue( element,
                                          Constants.COMPONENT_ANNOTATION_CLASSNAME,
                                          "disposeNotifier" ).getValue();
    switch ( variableElement.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return !AnnotationsUtil.hasAnnotationOfType( element, Constants.SINGLETON_ANNOTATION_CLASSNAME );
    }
  }

  public static boolean anyParametersNamed( @Nonnull final ExecutableElement element, @Nonnull final String name )
  {
    return element.getParameters().stream().anyMatch( p -> p.getSimpleName().toString().equals( name ) );
  }
}
