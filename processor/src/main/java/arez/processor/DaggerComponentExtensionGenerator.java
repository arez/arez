package arez.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;

final class DaggerComponentExtensionGenerator
{
  private DaggerComponentExtensionGenerator()
  {
  }

  @Nonnull
  static TypeSpec buildType( @Nonnull final ProcessingEnvironment processingEnv,
                             @Nonnull final ComponentDescriptor descriptor )
  {
    assert descriptor.shouldGenerateFactory();
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( descriptor.getDaggerComponentExtensionClassName() );
    GeneratorUtil.copyWhitelistedAnnotations( descriptor.getElement(), builder );
    GeneratorUtil.addGeneratedAnnotation( processingEnv, builder, ArezProcessor.class.getName() );
    GeneratorUtil.addOriginatingTypes( descriptor.getElement(), builder );

    builder.addModifiers( Modifier.PUBLIC );

    builder.addMethod( MethodSpec
                         .methodBuilder( "get" + descriptor.getType() + "DaggerSubcomponent" )
                         .addModifiers( Modifier.PUBLIC, Modifier.ABSTRACT )
                         .returns( ClassName.bestGuess( "DaggerSubcomponent" ) )
                         .build() );
    builder.addType( buildFactoryBasedDaggerSubcomponent( descriptor ) );

    return builder.build();
  }

  @Nonnull
  private static TypeSpec buildFactoryBasedDaggerSubcomponent( @Nonnull final ComponentDescriptor descriptor )
  {
    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( "DaggerSubcomponent" );

    builder.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    builder.addAnnotation( AnnotationSpec.builder( ClassName.get( "dagger", "Subcomponent" ) ).build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "createFactory" )
                         .addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC )
                         .returns( descriptor.getEnhancedClassName().nestedClass( "Factory" ) )
                         .build() );

    builder.addMethod( MethodSpec
                         .methodBuilder( "inject" )
                         .addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC )
                         .addParameter( ParameterSpec
                                          .builder( descriptor.getEnhancedClassName(), "component" )
                                          .addAnnotation( GeneratorUtil.NONNULL_CLASSNAME )
                                          .build() )
                         .build() );

    return builder.build();
  }
}
