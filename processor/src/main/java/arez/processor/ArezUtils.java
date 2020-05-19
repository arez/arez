package arez.processor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
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
                                                           Constants.COMPONENT_CLASSNAME,
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
    MemberChecks.mustBeSubclassCallable( targetType, Constants.COMPONENT_CLASSNAME, annotationName, method );
    MemberChecks.mustNotReturnAnyValue( annotationName, method );
    MemberChecks.mustNotThrowAnyExceptions( annotationName, method );
  }

  static void shouldBeInternalRefMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                         @Nonnull final ComponentDescriptor descriptor,
                                         @Nonnull final ExecutableElement method,
                                         @Nonnull final String annotationClassname )
  {
    if ( MemberChecks.doesMethodNotOverrideInterfaceMethod( processingEnv, descriptor.getElement(), method ) )
    {
      MemberChecks.shouldNotBePublic( processingEnv,
                                      method,
                                      annotationClassname,
                                      Constants.WARNING_PUBLIC_REF_METHOD,
                                      Constants.SUPPRESS_AREZ_WARNINGS_CLASSNAME );
    }
  }

  static void shouldBeInternalLifecycleMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                               @Nonnull final ComponentDescriptor descriptor,
                                               @Nonnull final ExecutableElement method,
                                               @Nonnull final String annotationClassname )
  {
    if ( MemberChecks.doesMethodNotOverrideInterfaceMethod( processingEnv, descriptor.getElement(), method ) )
    {
      MemberChecks.shouldNotBePublic( processingEnv,
                                      method,
                                      annotationClassname,
                                      Constants.WARNING_PUBLIC_LIFECYCLE_METHOD,
                                      Constants.SUPPRESS_AREZ_WARNINGS_CLASSNAME );
    }
  }

  static void shouldBeInternalHookMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                          @Nonnull final ComponentDescriptor descriptor,
                                          @Nonnull final ExecutableElement method,
                                          @Nonnull final String annotationClassname )
  {
    if ( MemberChecks.doesMethodNotOverrideInterfaceMethod( processingEnv, descriptor.getElement(), method ) )
    {
      MemberChecks.shouldNotBePublic( processingEnv,
                                      method,
                                      annotationClassname,
                                      Constants.WARNING_PUBLIC_HOOK_METHOD,
                                      Constants.SUPPRESS_AREZ_WARNINGS_CLASSNAME );
    }
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

  public static boolean anyParametersNamed( @Nonnull final ExecutableElement element, @Nonnull final String name )
  {
    return element.getParameters().stream().anyMatch( p -> p.getSimpleName().toString().equals( name ) );
  }

  //TODO: Move to proton
  @Nullable
  static ExecutableElement getOverriddenMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                                @Nonnull final TypeElement typeElement,
                                                @Nonnull final ExecutableElement method )
  {
    final TypeMirror superclass = typeElement.getSuperclass();
    if ( TypeKind.NONE == superclass.getKind() )
    {
      return null;
    }
    else
    {
      final TypeElement parent = (TypeElement) processingEnv.getTypeUtils().asElement( superclass );
      final List<? extends Element> enclosedElements = parent.getEnclosedElements();
      for ( final Element enclosedElement : enclosedElements )
      {
        if ( ElementKind.METHOD == enclosedElement.getKind() &&
             processingEnv.getElementUtils().overrides( method, (ExecutableElement) enclosedElement, typeElement ) )
        {
          return (ExecutableElement) enclosedElement;
        }
      }
      return getOverriddenMethod( processingEnv, parent, method );
    }
  }
}
