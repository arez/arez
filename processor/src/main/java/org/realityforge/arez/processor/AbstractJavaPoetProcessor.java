package org.realityforge.arez.processor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.Element;
import static javax.tools.Diagnostic.Kind.ERROR;

abstract class AbstractJavaPoetProcessor
  extends AbstractProcessor
{
  final void processElements( @Nonnull final Set<? extends Element> elements )
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
        e.print( processingEnv.getMessager() );
      }
    }
  }

  protected abstract void process( @Nonnull Element element )
    throws IOException, ArezProcessorException;

  final void emitTypeSpec( @Nonnull final String packageName, @Nonnull final TypeSpec typeSpec )
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
}
