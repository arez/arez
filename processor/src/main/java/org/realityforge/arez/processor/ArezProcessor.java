package org.realityforge.arez.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import org.realityforge.arez.annotations.Container;

/**
 * Annotation processor that analyzes Arez annotated source and generates Observable models.
 */
@AutoService( Processor.class )
@SupportedAnnotationTypes( { "org.realityforge.arez.annotations.Action",
                             "org.realityforge.arez.annotations.Computed",
                             "org.realityforge.arez.annotations.Container",
                             "org.realityforge.arez.annotations.ContainerId",
                             "org.realityforge.arez.annotations.Observable" } )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
public final class ArezProcessor
  extends AbstractJavaPoetProcessor
{
  private static final ClassName AREZ_CONTEXT_CLASSNAME = ClassName.get( "org.realityforge.arez", "ArezContext" );
  private static final String CONTEXT_FIELD_NAME = "$$arez$$_context";

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean process( final Set<? extends TypeElement> annotations, final RoundEnvironment env )
  {
    final Set<? extends Element> elements = env.getElementsAnnotatedWith( Container.class );
    processElements( elements );
    return false;
  }

  @Override
  protected void process( @Nonnull final Element element )
    throws IOException, ArezProcessorException
  {
    final ContainerDescriptor descriptor =
      ContainerDescriptorParser.parse( element, processingEnv.getElementUtils(), processingEnv.getTypeUtils() );
    emitTypeSpec( descriptor.getPackageElement().getQualifiedName().toString(), builder( descriptor ) );
  }

  @Nonnull
  private TypeSpec builder( @Nonnull final ContainerDescriptor descriptor )
    throws ArezProcessorException
  {
    final TypeElement element = descriptor.getElement();

    final AnnotationSpec generatedAnnotation =
      AnnotationSpec.builder( Generated.class ).addMember( "value", "$S", getClass().getName() ).build();

    final TypeSpec.Builder builder = TypeSpec.classBuilder( "Arez_" + element.getSimpleName() ).
      superclass( TypeName.get( element.asType() ) ).
      addTypeVariables( ProcessorUtil.getTypeArgumentsAsNames( descriptor.asDeclaredType() ) ).
      addModifiers( Modifier.FINAL ).
      addAnnotation( generatedAnnotation );
    ProcessorUtil.copyAccessModifiers( element, builder );

    for ( final ExecutableElement constructor : ProcessorUtil.getConstructors( element ) )
    {
      builder.addMethod( buildConstructor( constructor ) );
    }

    return builder.build();
  }

  @Nonnull
  private MethodSpec buildConstructor( @Nonnull final ExecutableElement constructor )
  {
    final MethodSpec.Builder builder = MethodSpec.constructorBuilder();
    ProcessorUtil.copyAccessModifiers( constructor, builder );

    final StringBuilder superCall = new StringBuilder();
    superCall.append( "super(" );
    final ArrayList<String> parameterNames = new ArrayList<>();

    // Add the first context class parameter
    {
      final ParameterSpec.Builder param =
        ParameterSpec.builder( AREZ_CONTEXT_CLASSNAME, CONTEXT_FIELD_NAME, Modifier.FINAL ).
          addAnnotation( Nonnull.class );
      builder.addParameter( param.build() );
    }

    boolean firstParam = true;
    for ( final VariableElement element : constructor.getParameters() )
    {
      final ParameterSpec.Builder param =
        ParameterSpec.builder( TypeName.get( element.asType() ), element.getSimpleName().toString(), Modifier.FINAL );
      ProcessorUtil.copyDocumentedAnnotations( element, param );
      builder.addParameter( param.build() );
      parameterNames.add( element.getSimpleName().toString() );
      if ( !firstParam )
      {
        superCall.append( "," );
      }
      firstParam = false;
      superCall.append( "$N" );
    }

    superCall.append( ")" );
    builder.addStatement( superCall.toString(), parameterNames.toArray() );

    return builder.build();
  }
}
