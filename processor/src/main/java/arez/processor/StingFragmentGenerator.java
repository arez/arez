package arez.processor;

import com.squareup.javapoet.AnnotationSpec;
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
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import org.realityforge.proton.AnnotationsUtil;
import org.realityforge.proton.ElementsUtil;
import org.realityforge.proton.GeneratorUtil;
import org.realityforge.proton.ProcessorException;
import org.realityforge.proton.SuppressWarningsUtil;

final class StingFragmentGenerator
{
  private StingFragmentGenerator()
  {
  }

  @Nonnull
  static TypeSpec buildType( @Nonnull final ProcessingEnvironment processingEnv,
                             @Nonnull final ComponentDescriptor component )
    throws ProcessorException
  {
    assert component.isStingEnabled();

    final TypeSpec.Builder builder = TypeSpec.interfaceBuilder( component.getStingFragmentName() );

    GeneratorUtil.copyWhitelistedAnnotations( component.getElement(), builder );
    GeneratorUtil.addOriginatingTypes( component.getElement(), builder );

    GeneratorUtil.addGeneratedAnnotation( processingEnv, builder, ArezProcessor.class.getName() );
    builder.addAnnotation( ClassName.get( "sting", "Fragment" ) );
    builder.addModifiers( Modifier.PUBLIC );
    builder.addMethod( buildCreateMethod( processingEnv, component ) );

    return builder.build();
  }

  @SuppressWarnings( "unchecked" )
  @Nonnull
  private static MethodSpec buildCreateMethod( @Nonnull final ProcessingEnvironment processingEnv,
                                               @Nonnull final ComponentDescriptor component )
  {
    final MethodSpec.Builder method = MethodSpec.methodBuilder( "create" ).
      addModifiers( Modifier.DEFAULT, Modifier.PUBLIC ).
      addAnnotation( GeneratorUtil.NONNULL_CLASSNAME ).
      returns( ClassName.get( component.getElement() ) );

    final List<String> whitelist = new ArrayList<>( GeneratorUtil.ANNOTATION_WHITELIST );
    whitelist.add( Constants.STING_NAMED );
    whitelist.add( Constants.STING_EAGER );

    GeneratorUtil.copyWhitelistedAnnotations( component.getElement(), method, whitelist );

    final AnnotationMirror typed =
      AnnotationsUtil.findAnnotationByType( component.getElement(), Constants.STING_TYPED );
    if ( null == typed )
    {
      method.addAnnotation( AnnotationSpec
                              .builder( ClassName.get( "sting", "Typed" ) )
                              .addMember( "value", "$T.class", component.getClassName() )
                              .build() );
    }
    else if ( ( (List<AnnotationValue>) AnnotationsUtil.getAnnotationValue( typed, "value" ).getValue() ).isEmpty() )
    {
      // This is only needed because javapoet has a bug and does not correctly the scenario
      // where the attribute is an array, and it is empty and there is no default value
      method.addAnnotation( AnnotationSpec
                              .builder( ClassName.get( "sting", "Typed" ) )
                              .addMember( "value", "{}" )
                              .build() );
    }
    else
    {
      method.addAnnotation( AnnotationSpec.get( typed ) );
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
    whitelistedAnnotations.add( "sting.Named" );
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

    return method.build();
  }
}
