package arez.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import org.realityforge.proton.ElementsUtil;
import org.realityforge.proton.GeneratorUtil;
import org.realityforge.proton.ProcessorException;
import org.realityforge.proton.SuppressWarningsUtil;

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
    assert component.isDaggerEnabled();

    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( component.getComponentDaggerModuleName() );
    GeneratorUtil.copyWhitelistedAnnotations( component.getElement(), builder );
    GeneratorUtil.addOriginatingTypes( component.getElement(), builder );

    GeneratorUtil.addGeneratedAnnotation( processingEnv, builder, ArezProcessor.class.getName() );
    builder.addAnnotation( ClassName.get( "dagger", "Module" ) );
    builder.addModifiers( Modifier.PUBLIC );

    final MethodSpec.Builder method = MethodSpec.methodBuilder( "create" ).
      addAnnotation( ClassName.get( "dagger", "Provides" ) ).
      addModifiers( Modifier.STATIC, Modifier.PUBLIC ).
      addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
      returns( ClassName.get( component.getElement() ) );
    final AnnotationMirror scopeAnnotation = component.getScopeAnnotation();
    if ( null != scopeAnnotation )
    {
      final DeclaredType annotationType = scopeAnnotation.getAnnotationType();
      final TypeElement typeElement = (TypeElement) annotationType.asElement();
      method.addAnnotation( ClassName.get( typeElement ) );
    }

    final ExecutableElement constructor = ElementsUtil.getConstructors( component.getElement() ).get( 0 );

    SuppressWarningsUtil.addSuppressWarningsIfRequired( processingEnv,
                                                        method,
                                                        Collections.singletonList( constructor.asType() ) );

    final List<Object> args = new ArrayList<>();
    final StringBuilder sb = new StringBuilder();
    sb.append( "return new $T(" );
    args.add( component.getEnhancedClassName() );

    final List<String> whitelistedAnnotations = new ArrayList<>( GeneratorUtil.ANNOTATION_WHITELIST );
    whitelistedAnnotations.add( "javax.inject.Named" );

    boolean firstParam = true;

    for ( final VariableElement parameter : constructor.getParameters() )
    {
      final String name = parameter.getSimpleName().toString();
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( parameter.asType() ), name, Modifier.FINAL );

      GeneratorUtil.copyWhitelistedAnnotations( parameter, param, whitelistedAnnotations );
      method.addParameter( param.build() );
      if ( !firstParam )
      {
        sb.append( ", " );
      }
      else
      {
        firstParam = false;
      }
      sb.append( "$N" );
      args.add( name );
    }
    sb.append( ")" );
    method.addStatement( sb.toString(), args.toArray() );

    builder.addMethod( method.build() );
    return builder.build();
  }
}
