package org.realityforge.arez.processor;

import javax.annotation.Nonnull;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import org.realityforge.arez.annotations.Container;

final class ContainerDescriptorParser
{
  private ContainerDescriptorParser()
  {
  }

  static ContainerDescriptor parse( @Nonnull final Element element,
                                    @Nonnull final Elements elementUtils,
                                    @Nonnull final Types typeUtils )
    throws ArezProcessorException
  {
    final PackageElement packageElement = elementUtils.getPackageOf( element );
    final TypeElement typeElement = (TypeElement) element;
    if ( !element.getKind().isClass() )
    {
      throw new ArezProcessorException( "@Container target must be a class", element );
    }
    else if ( element.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ArezProcessorException( "@Container target must not be abstract", element );
    }
    else if ( element.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@Container target must not be final", element );
    }
    final Container container = typeElement.getAnnotation( Container.class );
    final String name =
      container.name().equals( "<default>" ) ? typeElement.getSimpleName().toString() : container.name();

    return new ContainerDescriptor( name, container.singleton(), packageElement, typeElement );
  }
}
