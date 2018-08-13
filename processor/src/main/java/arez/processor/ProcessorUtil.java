package arez.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
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
import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

final class ProcessorUtil
{
  static final String SENTINEL_NAME = "<default>";

  private ProcessorUtil()
  {
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
  static PackageElement getPackageElement( @Nonnull final TypeElement element )
  {
    Element enclosingElement = element.getEnclosingElement();
    while ( null != enclosingElement )
    {
      if ( enclosingElement instanceof PackageElement )
      {
        return (PackageElement) enclosingElement;
      }
      enclosingElement = enclosingElement.getEnclosingElement();
    }
    assert false;
    return null;
  }

  @Nonnull
  static List<TypeVariableName> getTypeArgumentsAsNames( @Nonnull final DeclaredType declaredType )
  {
    final List<TypeVariableName> variables = new ArrayList<>();
    for ( final TypeMirror argument : declaredType.getTypeArguments() )
    {
      variables.add( TypeVariableName.get( (TypeVariable) argument ) );
    }
    return variables;
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

  static void copyAccessModifiers( @Nonnull final TypeElement element, @Nonnull final TypeSpec.Builder builder )
  {
    if ( element.getModifiers().contains( Modifier.PUBLIC ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
  }

  static void copyAccessModifiers( @Nonnull final TypeElement element, @Nonnull final MethodSpec.Builder builder )
  {
    if ( element.getModifiers().contains( Modifier.PUBLIC ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
  }

  static void copyAccessModifiers( @Nonnull final ExecutableElement element, @Nonnull final MethodSpec.Builder builder )
  {
    if ( element.getModifiers().contains( Modifier.PUBLIC ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    else if ( element.getModifiers().contains( Modifier.PROTECTED ) )
    {
      builder.addModifiers( Modifier.PROTECTED );
    }
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final MethodSpec.Builder builder )
  {
    for ( final AnnotationMirror annotation : element.getAnnotationMirrors() )
    {
      if ( shouldCopyAnnotation( annotation.getAnnotationType().toString() ) )
      {
        builder.addAnnotation( AnnotationSpec.get( annotation ) );
      }
    }
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final ParameterSpec.Builder builder )
  {
    for ( final AnnotationMirror annotation : element.getAnnotationMirrors() )
    {
      if ( shouldCopyAnnotation( annotation.getAnnotationType().toString() ) )
      {
        builder.addAnnotation( AnnotationSpec.get( annotation ) );
      }
    }
  }

  private static boolean shouldCopyAnnotation( @Nonnull final String classname )
  {
    return Constants.NONNULL_ANNOTATION_CLASSNAME.equals( classname ) ||
           Constants.NULLABLE_ANNOTATION_CLASSNAME.equals( classname ) ||
           Constants.DEPRECATED_ANNOTATION_CLASSNAME.equals( classname );
  }

  static void copyExceptions( @Nonnull final ExecutableType method, @Nonnull final MethodSpec.Builder builder )
  {
    for ( final TypeMirror thrownType : method.getThrownTypes() )
    {
      builder.addException( TypeName.get( thrownType ) );
    }
  }

  static void copyTypeParameters( @Nonnull final ExecutableType action, @Nonnull final MethodSpec.Builder builder )
  {
    for ( final TypeVariable typeParameter : action.getTypeVariables() )
    {
      builder.addTypeVariable( TypeVariableName.get( typeParameter ) );
    }
  }

  @Nullable
  static String deriveName( @Nonnull final ExecutableElement method,
                            @Nonnull final Pattern pattern,
                            @Nonnull final String name )
    throws ArezProcessorException
  {
    if ( isSentinelName( name ) )
    {
      final String methodName = method.getSimpleName().toString();
      final Matcher matcher = pattern.matcher( methodName );
      if ( matcher.find() )
      {
        final String candidate = matcher.group( 1 );
        return Character.toLowerCase( candidate.charAt( 0 ) ) + candidate.substring( 1 );
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

  static boolean isSentinelName( @Nonnull final String name )
  {
    return SENTINEL_NAME.equals( name );
  }

  @SuppressWarnings( { "unchecked", "SameParameterValue" } )
  @Nonnull
  static List<TypeMirror> getTypeMirrorsAnnotationParameter( @Nonnull final Elements elements,
                                                             @Nonnull final TypeElement typeElement,
                                                             @Nonnull final String annotationClassName,
                                                             @Nonnull final String parameterName )
  {
    final AnnotationValue annotationValue =
      getAnnotationValue( elements, typeElement, annotationClassName, parameterName );
    return ( (List<AnnotationValue>) annotationValue.getValue() )
      .stream()
      .map( v -> (TypeMirror) v.getValue() ).collect( Collectors.toList() );
  }

  @Nonnull
  static AnnotationValue getAnnotationValue( @Nonnull final Elements elements,
                                             @Nonnull final AnnotatedConstruct annotated,
                                             @Nonnull final String annotationClassName,
                                             @Nonnull final String parameterName )
  {
    final AnnotationValue value = findAnnotationValue( elements, annotated, annotationClassName, parameterName );
    assert null != value;
    return value;
  }

  @Nullable
  private static AnnotationValue findAnnotationValue( @Nonnull final Elements elements,
                                                      @Nonnull final AnnotatedConstruct annotated,
                                                      @Nonnull final String annotationClassName,
                                                      @Nonnull final String parameterName )
  {
    final AnnotationMirror mirror = findAnnotationByType( annotated, annotationClassName );
    return null == mirror ? null : findAnnotationValue( elements, mirror, parameterName );
  }

  @Nullable
  private static AnnotationValue findAnnotationValue( @Nonnull final Elements elements,
                                                      @Nonnull final AnnotationMirror annotation,
                                                      @Nonnull final String parameterName )
  {
    final Map<? extends ExecutableElement, ? extends AnnotationValue> values =
      elements.getElementValuesWithDefaults( annotation );
    final ExecutableElement annotationKey = values.keySet().stream().
      filter( k -> parameterName.equals( k.getSimpleName().toString() ) ).findFirst().orElse( null );
    return values.get( annotationKey );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nullable
  static AnnotationValue findAnnotationValueNoDefaults( @Nonnull final AnnotationMirror annotation,
                                                        @Nonnull final String parameterName )
  {
    final Map<? extends ExecutableElement, ? extends AnnotationValue> values = annotation.getElementValues();
    final ExecutableElement annotationKey = values.keySet().stream().
      filter( k -> parameterName.equals( k.getSimpleName().toString() ) ).findFirst().orElse( null );
    return values.get( annotationKey );
  }

  @SuppressWarnings( "unchecked" )
  @Nonnull
  static <T> T getAnnotationValue( @Nonnull final Elements elements,
                                   @Nonnull final AnnotationMirror annotation,
                                   @Nonnull final String parameterName )
  {
    final AnnotationValue value = findAnnotationValue( elements, annotation, parameterName );
    assert null != value;
    return (T) value.getValue();
  }

  @Nonnull
  static AnnotationMirror getAnnotationByType( @Nonnull final Element typeElement,
                                               @Nonnull final String annotationClassName )
  {
    AnnotationMirror mirror = findAnnotationByType( typeElement, annotationClassName );
    assert null != mirror;
    return mirror;
  }

  @Nullable
  static AnnotationMirror findAnnotationByType( @Nonnull final AnnotatedConstruct annotated,
                                                @Nonnull final String annotationClassName )
  {
    return annotated.getAnnotationMirrors().stream().
      filter( a -> a.getAnnotationType().toString().equals( annotationClassName ) ).findFirst().orElse( null );
  }

  @Nonnull
  static String toSimpleName( @Nonnull final String annotationName )
  {
    return annotationName.replaceAll( ".*\\.", "" );
  }

  static boolean isDisposableTrackableRequired( @Nonnull final Elements elementUtils,
                                                @Nonnull final TypeElement typeElement )
  {
    final VariableElement variableElement = (VariableElement)
      getAnnotationValue( elementUtils,
                          typeElement,
                          Constants.COMPONENT_ANNOTATION_CLASSNAME,
                          "disposeTrackable" ).getValue();
    switch ( variableElement.getSimpleName().toString() )
    {
      case "ENABLE":
        return true;
      case "DISABLE":
        return false;
      default:
        return null == findAnnotationByType( typeElement, Constants.SINGLETON_ANNOTATION_CLASSNAME );
    }
  }
}
