package arez.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

final class ProcessorUtil
{
  private ProcessorUtil()
  {
  }

  @SuppressWarnings( "unchecked" )
  static boolean isWarningSuppressed( @Nonnull final Element element,
                                      @Nonnull final String warning,
                                      @Nullable final String alternativeSuppressWarnings )
  {
    if ( null != alternativeSuppressWarnings )
    {
      final AnnotationMirror suppress = AnnotationsUtil.findAnnotationByType( element, alternativeSuppressWarnings );
      if ( null != suppress )
      {
        final AnnotationValue value = AnnotationsUtil.findAnnotationValueNoDefaults( suppress, "value" );
        if ( null != value )
        {
          final List<AnnotationValue> warnings = (List<AnnotationValue>) value.getValue();
          for ( final AnnotationValue suppression : warnings )
          {
            if ( warning.equals( suppression.getValue() ) )
            {
              return true;
            }
          }
        }
      }
    }

    final SuppressWarnings annotation = element.getAnnotation( SuppressWarnings.class );
    if ( null != annotation )
    {
      for ( final String suppression : annotation.value() )
      {
        if ( warning.equals( suppression ) )
        {
          return true;
        }
      }
    }
    final Element enclosingElement = element.getEnclosingElement();
    return null != enclosingElement && isWarningSuppressed( enclosingElement, warning, alternativeSuppressWarnings );
  }

  @Nonnull
  static List<TypeElement> getSuperTypes( @Nonnull final TypeElement element )
  {
    final List<TypeElement> superTypes = new ArrayList<>();
    enumerateSuperTypes( element, superTypes );
    return superTypes;
  }

  private static void enumerateSuperTypes( @Nonnull final TypeElement element,
                                           @Nonnull final List<TypeElement> superTypes )
  {
    final TypeMirror superclass = element.getSuperclass();
    if ( TypeKind.NONE != superclass.getKind() )
    {
      final TypeElement superclassElement = (TypeElement) ( (DeclaredType) superclass ).asElement();
      superTypes.add( superclassElement );
      enumerateSuperTypes( superclassElement, superTypes );
    }
    for ( final TypeMirror interfaceType : element.getInterfaces() )
    {
      final TypeElement interfaceElement = (TypeElement) ( (DeclaredType) interfaceType ).asElement();
      enumerateSuperTypes( interfaceElement, superTypes );
    }
  }

  @Nonnull
  static List<VariableElement> getFieldElements( @Nonnull final TypeElement element )
  {
    final Map<String, VariableElement> methodMap = new LinkedHashMap<>();
    enumerateFieldElements( element, methodMap );
    return new ArrayList<>( methodMap.values() );
  }

  private static void enumerateFieldElements( @Nonnull final TypeElement element,
                                              @Nonnull final Map<String, VariableElement> fields )
  {
    final TypeMirror superclass = element.getSuperclass();
    if ( TypeKind.NONE != superclass.getKind() )
    {
      enumerateFieldElements( (TypeElement) ( (DeclaredType) superclass ).asElement(), fields );
    }
    for ( final Element member : element.getEnclosedElements() )
    {
      if ( member.getKind() == ElementKind.FIELD )
      {
        fields.put( member.getSimpleName().toString(), (VariableElement) member );
      }
    }
  }

  @Nonnull
  static List<ExecutableElement> getMethods( @Nonnull final TypeElement element,
                                             @Nonnull final Elements elementUtils,
                                             @Nonnull final Types typeUtils )
  {
    final Map<String, ArrayList<ExecutableElement>> methodMap = new LinkedHashMap<>();
    enumerateMethods( element, elementUtils, typeUtils, element, methodMap );
    return methodMap.values().stream().flatMap( Collection::stream ).collect( Collectors.toList() );
  }

  private static void enumerateMethods( @Nonnull final TypeElement scope,
                                        @Nonnull final Elements elementUtils,
                                        @Nonnull final Types typeUtils,
                                        @Nonnull final TypeElement element,
                                        @Nonnull final Map<String, ArrayList<ExecutableElement>> methods )
  {
    final TypeMirror superclass = element.getSuperclass();
    if ( TypeKind.NONE != superclass.getKind() )
    {
      final TypeElement superclassElement = (TypeElement) ( (DeclaredType) superclass ).asElement();
      enumerateMethods( scope, elementUtils, typeUtils, superclassElement, methods );
    }
    for ( final TypeMirror interfaceType : element.getInterfaces() )
    {
      final TypeElement interfaceElement = (TypeElement) ( (DeclaredType) interfaceType ).asElement();
      enumerateMethods( scope, elementUtils, typeUtils, interfaceElement, methods );
    }
    for ( final Element member : element.getEnclosedElements() )
    {
      if ( member.getKind() == ElementKind.METHOD )
      {
        final ExecutableElement method = (ExecutableElement) member;
        processMethod( elementUtils, typeUtils, scope, methods, method );
      }
    }
  }

  private static void processMethod( @Nonnull final Elements elementUtils,
                                     @Nonnull final Types typeUtils,
                                     @Nonnull final TypeElement typeElement,
                                     @Nonnull final Map<String, ArrayList<ExecutableElement>> methods,
                                     @Nonnull final ExecutableElement method )
  {
    final ExecutableType methodType =
      (ExecutableType) typeUtils.asMemberOf( (DeclaredType) typeElement.asType(), method );

    final String key = method.getSimpleName().toString();
    final ArrayList<ExecutableElement> elements = methods.computeIfAbsent( key, k -> new ArrayList<>() );
    boolean found = false;
    final int size = elements.size();
    for ( int i = 0; i < size; i++ )
    {
      final ExecutableElement executableElement = elements.get( i );
      if ( method.equals( executableElement ) )
      {
        found = true;
        break;
      }
      else if ( isSubsignature( typeUtils, typeElement, methodType, executableElement ) )
      {
        if ( !isAbstractInterfaceMethod( method ) )
        {
          elements.set( i, method );
        }
        found = true;
        break;
      }
      else if ( elementUtils.overrides( method, executableElement, typeElement ) )
      {
        elements.set( i, method );
        found = true;
        break;
      }
    }
    if ( !found )
    {
      elements.add( method );
    }
  }

  private static boolean isAbstractInterfaceMethod( final @Nonnull ExecutableElement method )
  {
    return method.getModifiers().contains( Modifier.ABSTRACT ) &&
           ElementKind.INTERFACE == method.getEnclosingElement().getKind();
  }

  private static boolean isSubsignature( @Nonnull final Types typeUtils,
                                         @Nonnull final TypeElement typeElement,
                                         @Nonnull final ExecutableType methodType,
                                         @Nonnull final ExecutableElement candidate )
  {
    final ExecutableType candidateType =
      (ExecutableType) typeUtils.asMemberOf( (DeclaredType) typeElement.asType(), candidate );
    final boolean isEqual = methodType.equals( candidateType );
    final boolean isSubsignature = typeUtils.isSubsignature( methodType, candidateType );
    return isSubsignature || isEqual;
  }

  @Nonnull
  static List<ExecutableElement> getConstructors( @Nonnull final TypeElement element )
  {
    return element.getEnclosedElements().stream().
      filter( m -> m.getKind() == ElementKind.CONSTRUCTOR ).
      map( m -> (ExecutableElement) m ).
      collect( Collectors.toList() );
  }

  @Nullable
  static String deriveName( @Nonnull final ExecutableElement method,
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
  static String firstCharacterToLowerCase( @Nonnull final String name )
  {
    return Character.toLowerCase( name.charAt( 0 ) ) + name.substring( 1 );
  }

  static boolean hasNonnullAnnotation( @Nonnull final Element element )
  {
    return AnnotationsUtil.hasAnnotationOfType( element, Constants.NONNULL_ANNOTATION_CLASSNAME );
  }

  static boolean isDisposableTrackableRequired( @Nonnull final Element element )
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
}
