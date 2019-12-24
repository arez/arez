package arez.processor.support;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

public final class ElementsUtil
{
  private ElementsUtil()
  {
  }

  @SuppressWarnings( "unchecked" )
  public static boolean isWarningSuppressed( @Nonnull final Element element,
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
  public static List<TypeElement> getSuperTypes( @Nonnull final TypeElement element )
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
  public static List<TypeElement> getInterfaces( @Nonnull final TypeElement element )
  {
    final List<TypeElement> superTypes = new ArrayList<>();
    enumerateInterfaces( element, superTypes );
    return superTypes;
  }

  private static void enumerateInterfaces( @Nonnull final TypeElement element,
                                           @Nonnull final List<TypeElement> superTypes )
  {
    final TypeMirror superclass = element.getSuperclass();
    if ( TypeKind.NONE != superclass.getKind() )
    {
      final TypeElement superclassElement = (TypeElement) ( (DeclaredType) superclass ).asElement();
      enumerateInterfaces( superclassElement, superTypes );
    }
    for ( final TypeMirror interfaceType : element.getInterfaces() )
    {
      final TypeElement interfaceElement = (TypeElement) ( (DeclaredType) interfaceType ).asElement();
      superTypes.add( interfaceElement );
      enumerateInterfaces( interfaceElement, superTypes );
    }
  }

  @Nonnull
  public static List<VariableElement> getFields( @Nonnull final TypeElement element )
  {
    final Map<String, VariableElement> methodMap = new LinkedHashMap<>();
    enumerateFields( element, methodMap );
    return new ArrayList<>( methodMap.values() );
  }

  private static void enumerateFields( @Nonnull final TypeElement element,
                                       @Nonnull final Map<String, VariableElement> fields )
  {
    final TypeMirror superclass = element.getSuperclass();
    if ( TypeKind.NONE != superclass.getKind() )
    {
      enumerateFields( (TypeElement) ( (DeclaredType) superclass ).asElement(), fields );
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
  public static List<ExecutableElement> getMethods( @Nonnull final TypeElement element,
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

  private static boolean isAbstractInterfaceMethod( @Nonnull final ExecutableElement method )
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
  public static List<ExecutableElement> getConstructors( @Nonnull final TypeElement element )
  {
    return element.getEnclosedElements().stream().
      filter( m -> m.getKind() == ElementKind.CONSTRUCTOR ).
      map( m -> (ExecutableElement) m ).
      collect( Collectors.toList() );
  }

  public static boolean doesMethodOverrideInterfaceMethod( @Nonnull final Types typeUtils,
                                                           @Nonnull final TypeElement typeElement,
                                                           @Nonnull final ExecutableElement method )
  {
    return getInterfaces( typeElement ).stream()
      .flatMap( i -> i.getEnclosedElements().stream() )
      .filter( e1 -> e1 instanceof ExecutableElement )
      .map( e1 -> (ExecutableElement) e1 )
      .collect(
        Collectors.toList() ).stream()
      .anyMatch( e -> isSubsignature( typeUtils,
                                      typeElement,
                                      (ExecutableType) typeUtils.asMemberOf( (DeclaredType) typeElement.asType(), e ),
                                      method ) );
  }

  @Nonnull
  public static TypeName toRawType( @Nonnull final TypeMirror type )
  {
    final TypeName typeName = TypeName.get( type );
    if ( typeName instanceof ParameterizedTypeName )
    {
      return ( (ParameterizedTypeName) typeName ).rawType;
    }
    else
    {
      return typeName;
    }
  }
}
