package org.realityforge.arez.processor;

import javax.annotation.Nonnull;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import org.realityforge.arez.annotations.Container;

final class ContainerDescriptorParser
{
  private ContainerDescriptorParser()
  {
  }

  @Nonnull
  static ContainerDescriptor parse( final PackageElement packageElement, final TypeElement typeElement )
    throws ArezProcessorException
  {
    if ( ElementKind.CLASS != typeElement.getKind() )
    {
      throw new ArezProcessorException( "@Container target must be a class", typeElement );
    }
    else if ( typeElement.getModifiers().contains( Modifier.ABSTRACT ) )
    {
      throw new ArezProcessorException( "@Container target must not be abstract", typeElement );
    }
    else if ( typeElement.getModifiers().contains( Modifier.FINAL ) )
    {
      throw new ArezProcessorException( "@Container target must not be final", typeElement );
    }
    else if ( NestingKind.TOP_LEVEL != typeElement.getNestingKind() &&
              !typeElement.getModifiers().contains( Modifier.STATIC ) )
    {
      throw new ArezProcessorException( "@Container target must not be a non-static nested class", typeElement );
    }
    final Container container = typeElement.getAnnotation( Container.class );
    final String name =
      ProcessorUtil.isSentinelName( container.name() ) ? typeElement.getSimpleName().toString() : container.name();

    final ContainerDescriptor descriptor =
      new ContainerDescriptor( name, container.singleton(), container.disposable(), packageElement, typeElement );

    descriptor.analyzeCandidateMethods( ProcessorUtil.getMethods( typeElement ) );

    descriptor.validate();

    return descriptor;
  }
}
