package org.realityforge.arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

class ArezProcessorException
  extends Exception
{
  @Nonnull
  private final Element _element;

  ArezProcessorException( @Nonnull final String message, @Nonnull final Element element )
  {
    super( message );
    _element = Objects.requireNonNull( element );
  }

  @Nonnull
  Element getElement()
  {
    return _element;
  }

}
