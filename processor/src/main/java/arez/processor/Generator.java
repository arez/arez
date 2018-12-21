package arez.processor;

import com.google.auto.common.GeneratedAnnotationSpecs;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SuppressWarnings( "Duplicates" )
final class Generator
{
  private static final ClassName PROVIDER_CLASSNAME = ClassName.get( "javax.inject", "Provider" );
  static final ClassName NONNULL_CLASSNAME = ClassName.get( "javax.annotation", "Nonnull" );
  private static final ClassName NULLABLE_CLASSNAME = ClassName.get( "javax.annotation", "Nullable" );

  private Generator()
  {
  }

  @Nonnull
  static TypeSpec buildProviderDaggerComponentExtension( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( descriptor.getDaggerComponentExtensionClassName() );
    addGeneratedAnnotation( descriptor, builder );
    addOriginatingTypes( descriptor.getElement(), builder );

    builder.addModifiers( Modifier.PUBLIC );

    builder.addMethod( MethodSpec.methodBuilder( "createProvider" ).
      addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC ).
      returns( ParameterizedTypeName.get( PROVIDER_CLASSNAME, descriptor.getClassName() ) ).build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "inject" )
                         .addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC )
                         .addParameter( ParameterSpec
                                          .builder( descriptor.getEnhancedClassName(), "component" )
                                          .addAnnotation( NONNULL_CLASSNAME )
                                          .build() )
                         .build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "bind" + descriptor.getType() )
                         .addModifiers( Modifier.PUBLIC, Modifier.DEFAULT )
                         .addStatement( "InjectSupport.c_enhancer = this::inject" )
                         .build() );

    builder.addType( buildInjectSupport( descriptor ) );
    builder.addType( buildDaggerModule( descriptor ) );
    builder.addType( buildEnhancerDaggerModule( descriptor ) );

    return builder.build();
  }

  @Nonnull
  static TypeSpec buildConsumerDaggerComponentExtension( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( descriptor.getDaggerComponentExtensionClassName() );
    addGeneratedAnnotation( descriptor, builder );
    addOriginatingTypes( descriptor.getElement(), builder );

    builder.addModifiers( Modifier.PUBLIC );

    {
      final MethodSpec.Builder method =
        MethodSpec.methodBuilder( "get" + descriptor.getType() + "DaggerSubcomponent" ).
          addModifiers( Modifier.PUBLIC, Modifier.ABSTRACT ).
          returns( ClassName.bestGuess( "DaggerSubcomponent" ) );
      builder.addMethod( method.build() );
    }
    {
      final MethodSpec.Builder method =
        MethodSpec.methodBuilder( "bind" + descriptor.getType() ).
          addModifiers( Modifier.PUBLIC, Modifier.DEFAULT );
      method.addStatement( "InjectSupport.c_enhancer = instance -> $N().inject( instance )",
                           "get" + descriptor.getType() + "DaggerSubcomponent" );
      builder.addMethod( method.build() );
    }

    builder.addType( buildInjectSupport( descriptor ) );
    builder.addType( buildDaggerModule( descriptor ) );
    builder.addType( buildEnhancerDaggerModule( descriptor ) );
    builder.addType( buildConsumerDaggerSubcomponent( descriptor ) );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildConsumerDaggerSubcomponent( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( "DaggerSubcomponent" );

    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    builder.addAnnotation( AnnotationSpec
                             .builder( GeneratorUtil.DAGGER_SUBCOMPONENT_CLASSNAME )
                             .addMember( "modules", "DaggerModule.class" )
                             .build() );

    builder.addMethod( MethodSpec.methodBuilder( "createProvider" ).
      addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC ).
      returns( ParameterizedTypeName.get( PROVIDER_CLASSNAME, descriptor.getClassName() ) ).build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "inject" )
                         .addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC )
                         .addParameter( ParameterSpec
                                          .builder( descriptor.getEnhancedClassName(), "component" )
                                          .addAnnotation( NONNULL_CLASSNAME )
                                          .build() )
                         .build() );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildDaggerModule( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( "DaggerModule" );

    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    builder.addAnnotation( AnnotationSpec.builder( GeneratorUtil.DAGGER_MODULE_CLASSNAME )
                             .addMember( "includes", "EnhancerDaggerModule.class" )
                             .build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "bindComponent" )
                         .addAnnotation( GeneratorUtil.DAGGER_BINDS_CLASSNAME )
                         .addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC )
                         .addParameter( descriptor.getClassNameToConstruct(), "component" )
                         .returns( descriptor.getClassName() )
                         .build() );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildInjectSupport( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "InjectSupport" );

    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL );

    builder.addField( FieldSpec
                        .builder( descriptor.getEnhancedClassName().nestedClass( "Enhancer" ), "c_enhancer" )
                        .addModifiers( Modifier.PRIVATE, Modifier.STATIC ).build() );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildEnhancerDaggerModule( @Nonnull final ComponentDescriptor descriptor )
  {
    // Dues to a bug in javapoet that disallows static methods as part of interface classes
    // we synthesize a separate module and it include it in main component to achieve the same
    // goal. In an ideal world this static method would be merged into above class
    final TypeSpec.Builder builder = TypeSpec.classBuilder( "EnhancerDaggerModule" );

    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    builder.addAnnotation( GeneratorUtil.DAGGER_MODULE_CLASSNAME );

    builder.addMethod( MethodSpec
                         .methodBuilder( "provideEnhancer" )
                         .addAnnotation( GeneratorUtil.DAGGER_PROVIDES_CLASSNAME )
                         .addModifiers( Modifier.STATIC )
                         .returns( descriptor.getEnhancerClassName() )
                         .addStatement( "return InjectSupport.c_enhancer" )
                         .build() );

    return builder.build();
  }

  static void addOriginatingTypes( @Nonnull final TypeElement element, @Nonnull final TypeSpec.Builder builder )
  {
    builder.addOriginatingElement( element );
    ProcessorUtil.getSuperTypes( element ).forEach( builder::addOriginatingElement );
  }

  static void addGeneratedAnnotation( @Nonnull final ComponentDescriptor descriptor,
                                      @Nonnull final TypeSpec.Builder builder )
  {
    GeneratedAnnotationSpecs
      .generatedAnnotationSpec( descriptor.getElements(), descriptor.getSourceVersion(), ArezProcessor.class )
      .ifPresent( builder::addAnnotation );
  }
}
