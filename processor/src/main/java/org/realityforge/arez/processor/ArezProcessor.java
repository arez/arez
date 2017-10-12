package org.realityforge.arez.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Repository;
import static javax.tools.Diagnostic.Kind.ERROR;

/**
 * Annotation processor that analyzes Arez annotated source and generates models from the annotations.
 */
@SuppressWarnings( "Duplicates" )
@AutoService( Processor.class )
@SupportedAnnotationTypes( { "org.realityforge.arez.annotations.*", "javax.annotation.PostConstruct" } )
@SupportedSourceVersion( SourceVersion.RELEASE_8 )
public final class ArezProcessor
  extends AbstractProcessor
{
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean process( final Set<? extends TypeElement> annotations, final RoundEnvironment env )
  {
    final Set<? extends Element> elements = env.getElementsAnnotatedWith( ArezComponent.class );
    processElements( elements );
    return false;
  }

  private void processElements( @Nonnull final Set<? extends Element> elements )
  {
    for ( final Element element : elements )
    {
      try
      {
        process( element );
      }
      catch ( final IOException ioe )
      {
        processingEnv.getMessager().printMessage( ERROR, ioe.getMessage(), element );
      }
      catch ( final ArezProcessorException e )
      {
        processingEnv.getMessager().printMessage( ERROR, e.getMessage(), e.getElement() );
      }
      catch ( final Throwable e )
      {
        final StringWriter sw = new StringWriter();
        e.printStackTrace( new PrintWriter( sw ) );
        sw.flush();

        final String message =
          "Unexpected error will running the " + getClass().getName() + " processor. This has " +
          "resulted in a failure to process the code and has left the compiler in an invalid " +
          "state. Please report the failure to the developers so that it can be fixed.\n" +
          " Report the error at: https://github.com/realityforge/arez/issues\n" +
          "\n\n" +
          sw.toString();
        processingEnv.getMessager().printMessage( ERROR, message, element );
      }
    }
  }

  private void process( @Nonnull final Element element )
    throws IOException, ArezProcessorException
  {
    final PackageElement packageElement = processingEnv.getElementUtils().getPackageOf( element );
    final TypeElement typeElement = (TypeElement) element;
    final ComponentDescriptor descriptor = parse( packageElement, typeElement );
    emitTypeSpec( descriptor.getPackageName(), descriptor.buildType( processingEnv.getTypeUtils() ) );
    if ( descriptor.hasRepository() )
    {
      emitTypeSpec( descriptor.getPackageName(), descriptor.buildRepository( processingEnv.getTypeUtils() ) );
      emitTypeSpec( descriptor.getPackageName(), descriptor.buildRepositoryExtension() );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Nonnull
  private ComponentDescriptor parse( final PackageElement packageElement, final TypeElement typeElement )
    throws ArezProcessorException
  {
    if ( ElementKind.CLASS != typeElement.getKind() )
    {
      throw new ArezProcessorException( "@ArezComponent target must be a class", typeElement );
    }
    else if ( typeElement.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ArezProcessorException( "@ArezComponent target must not be abstract", typeElement );
    }
    else if ( typeElement.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@ArezComponent target must not be final", typeElement );
    }
    else if ( NestingKind.TOP_LEVEL != typeElement.getNestingKind() &&
              !typeElement.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@ArezComponent target must not be a non-static nested class", typeElement );
    }
    final ArezComponent arezComponent = typeElement.getAnnotation( ArezComponent.class );
    final String name =
      ProcessorUtil.isSentinelName( arezComponent.name() ) ?
      typeElement.getSimpleName().toString() :
      arezComponent.name();

    final List<ExecutableElement> methods = ProcessorUtil.getMethods( typeElement, processingEnv.getTypeUtils() );
    final ComponentDescriptor descriptor =
      new ComponentDescriptor( name,
                               arezComponent.singleton(),
                               arezComponent.disposable(),
                               arezComponent.allowEmpty(),
                               packageElement,
                               typeElement );

    descriptor.analyzeCandidateMethods( methods, processingEnv.getTypeUtils() );
    descriptor.validate();

    final Repository repository = typeElement.getAnnotation( Repository.class );
    if ( null != repository )
    {
      final List<TypeElement> extensions =
        ProcessorUtil.getTypeMirrorsAnnotationParameter( typeElement, "extensions", Repository.class ).stream().
          map( typeMirror -> (TypeElement) processingEnv.getTypeUtils().asElement( typeMirror ) ).
          collect( Collectors.toList() );
      descriptor.configureRepository( repository.name(), extensions );
    }

    return descriptor;
  }

  private void emitTypeSpec( @Nonnull final String packageName, @Nonnull final TypeSpec typeSpec )
    throws IOException
  {
    JavaFile.builder( packageName, typeSpec ).
      skipJavaLangImports( true ).
      build().
      writeTo( processingEnv.getFiler() );
  }
}
