package arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.Element;

class ArezProcessorException
  extends RuntimeException
{
  @Nonnull
  private final Element _element;

  /**
   * Private constructor to restrict the types that can be passed into exception. If any other types
   * are passed into this constructor then the error handling in {@link ArezProcessor} needs to be updated.
   */
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
