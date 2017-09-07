package org.realityforge.arez.processor;

import com.google.auto.service.AutoService;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
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
    throw new ArezProcessorException( "Not yet implemented", element );
  }
}
