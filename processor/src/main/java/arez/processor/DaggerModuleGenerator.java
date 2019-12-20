package arez.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

final class DaggerModuleGenerator
{
  private DaggerModuleGenerator()
  {
  }

  @Nonnull
  static TypeSpec buildType( @Nonnull final ProcessingEnvironment processingEnv,
                             @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    assert component.needsDaggerIntegration();

    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( component.getComponentDaggerModuleName() ).
      addTypeVariables( GeneratorUtil.getTypeArgumentsAsNames( component.asDeclaredType() ) );
    GeneratorUtil.copyWhitelistedAnnotations( component.getElement(), builder );
    GeneratorUtil.addOriginatingTypes( component.getElement(), builder );

    GeneratorUtil.addGeneratedAnnotation( processingEnv, builder, ArezProcessor.class.getName() );
    builder.addAnnotation( ClassName.get( "dagger", "Module" ) );
    builder.addModifiers( Modifier.PUBLIC );

    final MethodSpec.Builder method = MethodSpec.methodBuilder( "bindComponent" ).
      addAnnotation( ClassName.get( "dagger", "Binds" ) ).
      addModifiers( Modifier.ABSTRACT, Modifier.PUBLIC ).
      addParameter( component.getEnhancedClassName(), "component" ).
      returns( ClassName.get( component.getElement() ) );
    if ( null != component.getScopeAnnotation() )
    {
      final DeclaredType annotationType = component.getScopeAnnotation().getAnnotationType();
      final TypeElement typeElement = (TypeElement) annotationType.asElement();
      method.addAnnotation( ClassName.get( typeElement ) );
    }

    builder.addMethod( method.build() );
    return builder.build();
  }
}
