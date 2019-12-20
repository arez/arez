package arez.processor;

import com.google.auto.common.MoreElements;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

@SuppressWarnings( { "SameParameterValue",
                     "unused",
                     "WeakerAccess",
                     "RedundantSuppression",
                     "BooleanMethodIsAlwaysInverted" } )
final class GeneratorUtil
{
  static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );
  @Nonnull
  private static final List<String> ANNOTATION_WHITELIST =
    Arrays.asList( Constants.NONNULL_ANNOTATION_CLASSNAME,
                   Constants.NULLABLE_ANNOTATION_CLASSNAME,
                   Deprecated.class.getName() );

  private GeneratorUtil()
  {
  }

  @Nonnull
  static ClassName getGeneratedClassName( @Nonnull final ClassName className,
                                          @Nonnull final String prefix,
                                          @Nonnull final String postfix )
  {
    return ClassName.get( className.packageName(), getGeneratedSimpleClassName( className, prefix, postfix ) );
  }

  @Nonnull
  static String getGeneratedSimpleClassName( @Nonnull final ClassName className,
                                             @Nonnull final String prefix,
                                             @Nonnull final String postfix )
  {
    return getNestedClassPrefix( className ) + prefix + className.simpleName() + postfix;
  }

  @Nonnull
  private static String getNestedClassPrefix( @Nonnull final ClassName className )
  {
    final StringBuilder name = new StringBuilder();
    final List<String> simpleNames = className.simpleNames();
    if ( simpleNames.size() > 1 )
    {
      for ( final String simpleName : simpleNames.subList( 0, simpleNames.size() - 1 ) )
      {
        name.append( simpleName );
        name.append( "_" );
      }
    }
    return name.toString();
  }

  @Nonnull
  static ClassName getGeneratedClassName( @Nonnull final TypeElement element,
                                          @Nonnull final String prefix,
                                          @Nonnull final String postfix )
  {
    return ClassName.get( getQualifiedPackageName( element ), getGeneratedSimpleClassName( element, prefix, postfix ) );
  }

  @Nonnull
  static String getQualifiedPackageName( @Nonnull final TypeElement element )
  {
    return getPackageElement( element ).getQualifiedName().toString();
  }

  @Nonnull
  static String getGeneratedSimpleClassName( @Nonnull final TypeElement element,
                                             @Nonnull final String prefix,
                                             @Nonnull final String postfix )
  {
    return getNestedClassPrefix( element ) + prefix + element.getSimpleName() + postfix;
  }

  @Nonnull
  private static String getNestedClassPrefix( @Nonnull final TypeElement element )
  {
    final StringBuilder name = new StringBuilder();
    TypeElement t = element;
    while ( NestingKind.TOP_LEVEL != t.getNestingKind() )
    {
      t = (TypeElement) t.getEnclosingElement();
      name.insert( 0, t.getSimpleName() + "_" );
    }
    return name.toString();
  }

  @SuppressWarnings( "UnstableApiUsage" )
  @Nonnull
  static PackageElement getPackageElement( @Nonnull final TypeElement element )
  {
    return MoreElements.getPackage( element );
  }

  static void emitJavaType( @Nonnull final String packageName,
                            @Nonnull final TypeSpec typeSpec,
                            @Nonnull final Filer filer )
    throws IOException
  {
    JavaFile.builder( packageName, typeSpec ).
      skipJavaLangImports( true ).
      build().
      writeTo( filer );
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

  static void copyTypeParameters( @Nonnull final TypeElement element, @Nonnull final MethodSpec.Builder builder )
  {
    for ( final TypeParameterElement typeParameter : element.getTypeParameters() )
    {
      builder.addTypeVariable( TypeVariableName.get( typeParameter ) );
    }
  }

  static void copyTypeParameters( @Nonnull final TypeElement element, @Nonnull final TypeSpec.Builder builder )
  {
    for ( final TypeParameterElement typeParameter : element.getTypeParameters() )
    {
      builder.addTypeVariable( TypeVariableName.get( typeParameter ) );
    }
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final TypeSpec.Builder builder )
  {
    copyWhitelistedAnnotations( element, builder, ANNOTATION_WHITELIST );
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final TypeSpec.Builder builder,
                                          @Nonnull final List<String> whitelist )
  {
    for ( final AnnotationMirror annotation : element.getAnnotationMirrors() )
    {
      if ( whitelist.contains( annotation.getAnnotationType().toString() ) )
      {
        builder.addAnnotation( AnnotationSpec.get( annotation ) );
      }
    }
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final MethodSpec.Builder builder )
  {
    copyWhitelistedAnnotations( element, builder, ANNOTATION_WHITELIST );
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final MethodSpec.Builder builder,
                                          @Nonnull final List<String> whitelist )
  {
    for ( final AnnotationMirror annotation : element.getAnnotationMirrors() )
    {
      if ( whitelist.contains( annotation.getAnnotationType().toString() ) )
      {
        builder.addAnnotation( AnnotationSpec.get( annotation ) );
      }
    }
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final ParameterSpec.Builder builder )
  {
    copyWhitelistedAnnotations( element, builder, ANNOTATION_WHITELIST );
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final ParameterSpec.Builder builder,
                                          @Nonnull final List<String> whitelist )
  {
    for ( final AnnotationMirror annotation : element.getAnnotationMirrors() )
    {
      if ( whitelist.contains( annotation.getAnnotationType().toString() ) )
      {
        builder.addAnnotation( AnnotationSpec.get( annotation ) );
      }
    }
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final FieldSpec.Builder builder )
  {
    copyWhitelistedAnnotations( element, builder, ANNOTATION_WHITELIST );
  }

  static void copyWhitelistedAnnotations( @Nonnull final AnnotatedConstruct element,
                                          @Nonnull final FieldSpec.Builder builder,
                                          @Nonnull final List<String> whitelist )
  {
    for ( final AnnotationMirror annotation : element.getAnnotationMirrors() )
    {
      if ( whitelist.contains( annotation.getAnnotationType().toString() ) )
      {
        builder.addAnnotation( AnnotationSpec.get( annotation ) );
      }
    }
  }

  static void addOriginatingTypes( @Nonnull final TypeElement element, @Nonnull final TypeSpec.Builder builder )
  {
    builder.addOriginatingElement( element );
    ProcessorUtil.getSuperTypes( element ).forEach( builder::addOriginatingElement );
  }

  static void addGeneratedAnnotation( @Nonnull final ProcessingEnvironment processingEnv,
                                      @Nonnull final TypeSpec.Builder builder,
                                      @Nonnull final String classname )
  {
    final SourceVersion sourceVersion = processingEnv.getSourceVersion();
    final String annotationName =
      sourceVersion.compareTo( SourceVersion.RELEASE_8 ) > 0 ?
      "javax.annotation.processing.Generated" :
      "javax.annotation.Generated";
    final TypeElement annotation = processingEnv.getElementUtils().getTypeElement( annotationName );
    if ( null != annotation )
    {
      final AnnotationSpec annotationSpec =
        AnnotationSpec
          .builder( ClassName.get( annotation ) )
          .addMember( "value", "$S", classname )
          .build();
      builder.addAnnotation( annotationSpec );
    }
  }

  @Nonnull
  static MethodSpec.Builder refMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                       @Nonnull final TypeElement typeElement,
                                       @Nonnull final ExecutableElement original )
  {
    final ExecutableType originalExecutableType =
      (ExecutableType) processingEnv.getTypeUtils().asMemberOf( (DeclaredType) typeElement.asType(), original );
    final TypeMirror returnType = originalExecutableType.getReturnType();

    final String methodName = original.getSimpleName().toString();
    final MethodSpec.Builder method = MethodSpec.methodBuilder( methodName );
    method.addModifiers( Modifier.FINAL );
    if ( AnnotationsUtil.hasAnnotationOfType( original, Deprecated.class.getName() ) )
    {
      method.addAnnotation( Deprecated.class );
    }
    method.addAnnotation( Override.class );
    if ( !TypeName.get( returnType ).isPrimitive() )
    {
      // If @Nonnull is present on the class path then generate ref using it
      final TypeElement nonnull = processingEnv.getElementUtils().getTypeElement( "javax.annotation.Nonnull" );
      if ( null != nonnull )
      {
        method.addAnnotation( ClassName.get( "javax.annotation", "Nonnull" ) );
      }
    }

    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv, method, returnType );
    copyAccessModifiers( original, method );
    copyTypeParameters( originalExecutableType, method );
    method.returns( TypeName.get( returnType ) );
    return method;
  }

  static boolean areTypesInDifferentPackage( @Nonnull final TypeElement typeElement1,
                                             @Nonnull final TypeElement typeElement2 )
  {
    return !areTypesInSamePackage( typeElement1, typeElement2 );
  }

  static boolean areTypesInSamePackage( @Nonnull final TypeElement typeElement1,
                                        @Nonnull final TypeElement typeElement2 )
  {
    final PackageElement packageElement1 = getPackageElement( typeElement1 );
    final PackageElement packageElement2 = getPackageElement( typeElement2 );
    return Objects.equals( packageElement1.getQualifiedName(), packageElement2.getQualifiedName() );
  }
}
