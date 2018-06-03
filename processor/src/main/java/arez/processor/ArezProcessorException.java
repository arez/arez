package arez.processor;

import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

class ArezProcessorException
  extends RuntimeException
{
  @Nonnull
  private final Element _element;

  ArezProcessorException( @Nonnull final String message, @Nonnull final TypeElement element )
  {
    this( message, (Element) element );
  }

  ArezProcessorException( @Nonnull final String message, @Nonnull final ExecutableElement element )
  {
    this( message, (Element) element );
  }

  /**
   * Private constructor to restrict the types that can be passed into exception. If any other types
   * are passed into this constructor then the error handling in {@link ArezProcessor#processElements(Set, RoundEnvironment)}
   * needs to be updated.
   */
  private ArezProcessorException( @Nonnull final String message, @Nonnull final Element element )
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
