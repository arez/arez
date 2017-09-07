package org.realityforge.arez.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
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
    final AnnotationSpec generatedAnnotation =
      AnnotationSpec.builder( Generated.class ).addMember( "value", "$S", getClass().getName() ).build();

    final TypeSpec.Builder builder = TypeSpec.classBuilder( "Arez_" + descriptor.getElement().getSimpleName() ).
      addTypeVariables( ProcessorUtil.getTypeArgumentsAsNames( descriptor.asDeclaredType() ) ).
      addModifiers( Modifier.FINAL ).
      addAnnotation( generatedAnnotation );

    if ( descriptor.getElement().getModifiers().contains( Modifier.PUBLIC ) )
    {
      builder.addModifiers( Modifier.PUBLIC );
    }
    else if ( descriptor.getElement().getModifiers().contains( Modifier.PROTECTED ) )
    {
      builder.addModifiers( Modifier.PROTECTED );
    }
    else if ( descriptor.getElement().getModifiers().contains( Modifier.PRIVATE ) )
    {
      builder.addModifiers( Modifier.PRIVATE );
    }

    return builder.build();
  }
}
