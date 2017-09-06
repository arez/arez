package org.realityforge.arez.processor;

import javax.annotation.Nonnull;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

class ArezProcessorException
  extends Exception
{
  private final Element _element;

  ArezProcessorException( @Nonnull final String message, @Nonnull final Element element )
  {
    super( message );
    _element = element;
  }

  void print( @Nonnull final Messager messager )
  {
    if ( null != _element )
    {
      messager.printMessage( Diagnostic.Kind.ERROR, getMessage(), _element );
    }
    else
    {
      messager.printMessage( Diagnostic.Kind.ERROR, getMessage() );
    }
  }
}
