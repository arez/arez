package org.realityforge.arez.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import org.jetbrains.annotations.Contract;
import org.realityforge.arez.annotations.Container;
import static javax.tools.Diagnostic.Kind.ERROR;

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
  extends AbstractProcessor
{
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean process( final Set<? extends TypeElement> annotations, final RoundEnvironment env )
  {
    final Set<? extends Element> elements = env.getElementsAnnotatedWith( Container.class );
    for ( Element element : elements )
    {
      try
      {
        process( element );
      }
      catch ( final IOException ioe )
      {
        getMessager().printMessage( ERROR, ioe.getMessage() );
      }
      catch ( final ArezProcessorException e )
      {
        e.print( getMessager() );
      }
    }
    return false;
  }

  private void process( @Nonnull final Element element )
    throws IOException, ArezProcessorException
  {
    fail( "Not yet implemented", element );
  }

  @Contract( "_, _ -> fail" )
  private void fail( @Nonnull final String message, @Nonnull final Element element )
    throws ArezProcessorException
  {
    throw new ArezProcessorException( message, element );
  }

  private void emitTypeSpec( @Nonnull final String packageName, @Nonnull final TypeSpec typeSpec )
    throws IOException
  {
    emitJavaFile( convertTypeSpecToJavaFile( packageName, typeSpec ) );
  }

  @Nonnull
  private JavaFile convertTypeSpecToJavaFile( @Nonnull final String packageName, @Nonnull final TypeSpec typeSpec )
  {
    return JavaFile.builder( packageName, typeSpec ).
        skipJavaLangImports( true ).
        build();
  }

  private void emitJavaFile( @Nonnull final JavaFile javaFile )
    throws IOException
  {
    javaFile.writeTo( processingEnv.getFiler() );
  }

  @Nonnull
  private Elements getElements()
  {
    return processingEnv.getElementUtils();
  }

  @Nonnull
  private Messager getMessager()
  {
    return processingEnv.getMessager();
  }

  @Nonnull
  private Types getTypes()
  {
    return processingEnv.getTypeUtils();
  }
}
